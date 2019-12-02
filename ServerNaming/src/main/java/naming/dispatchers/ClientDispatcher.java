package naming.dispatchers;

import commons.StatusCodes;
import commons.commands.Command;
import commons.commands.naming.*;
import commons.commands.general.ErrorAck;
import commons.routines.IORoutines;
import naming.Node;
import naming.dispatchers.returns.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ClientDispatcher extends Thread {
    private int listeningPort;
    private ServerSocket server;
    private Dispatcher dispatcher;

    public ClientDispatcher(int listeningPort, Dispatcher dispatcher) {
        this.listeningPort = listeningPort;
        this.dispatcher = dispatcher;
    }

    private void serve() throws IOException {
        while (true) {
            Socket conn = server.accept();

            new Thread(() -> dispatch(conn)).start();
        }
    }

    private void dispatch(Socket conn) {
        try {
            Command command = IORoutines.receiveSignal(conn);
            Command ack = new ErrorAck();

            System.out.println("Client connected");

            if (command instanceof Init) {
                dispatcher.init();

                ack = new InitAck(StatusCodes.OK);

            } else if (command instanceof PutFile) {
                List<Node> nodes = dispatcher.getNodes();
                if (nodes.size() == 0) {
                    ack = new PutAck(StatusCodes.NO_NODES_AVAILABLE, null, null, null);
                    IORoutines.sendSignal(conn, ack);
                    return;
                }

                Path path = Paths.get(((PutFile) command).getRemotePath());
                if (path.getNameCount() == 0) {     // only root in the path
                    ack = new PutAck(StatusCodes.INCORRECT_NAME, null, null, null);
                    IORoutines.sendSignal(conn, ack);
                    return;
                }

                Path directoryPath = path.getParent();
                String fileName = path.getFileName().toString();

                long fileSize = ((PutFile) command).getSize();
                String fileRights = ((PutFile) command).getRights();
                PutReturnValue returnValue = dispatcher.put(directoryPath, fileName, false, fileSize, fileRights);
                if (returnValue.getStatus() == StatusCodes.OK) {
                    InetAddress storageAddress = nodes.get(0).getPublicIpAddress();
                    System.out.println("Storages public Address " + storageAddress);
                    List<InetAddress> replicaAddresses = new LinkedList<>();
                    for (int i = 1; i < nodes.size(); i++) {
                        replicaAddresses.add(nodes.get(i).getPrivateIpAddress());
                    }
                    ack = new PutAck(returnValue.getStatus(), storageAddress, returnValue.getFileId(), replicaAddresses);
                } else {
                    ack = new PutAck(returnValue.getStatus(), null, null, null);
                }

            } else if (command instanceof TouchFile) {
                Path path = Paths.get(((TouchFile) command).getNewPath());
                if (path.getNameCount() == 0) {     // only root in the path
                    ack = new TouchAck(StatusCodes.INCORRECT_NAME);
                    IORoutines.sendSignal(conn, ack);
                    return;
                }

                Path directoryPath = path.getParent();
                String fileName = path.getFileName().toString();

                TouchReturnValue returnValue = dispatcher.touch(directoryPath, fileName);
                ack = new TouchAck(returnValue.getStatus());

            } else if (command instanceof Get) {
                Path path = Paths.get(((Get) command).getFromPath());
                if (path.getNameCount() == 0) {     // only root in the path
                    ack = new GetAck(StatusCodes.INCORRECT_NAME, null, null);
                    IORoutines.sendSignal(conn, ack);
                    return;
                }

                GetReturnValue returnValue = dispatcher.get(path);
                if (returnValue.getStatus() == StatusCodes.OK) {
                    InetAddress nodeAddress = returnValue.getNode().getPublicIpAddress();
                    ack = new GetAck(returnValue.getStatus(), nodeAddress, returnValue.getFileId());
                } else {
                    ack = new GetAck(returnValue.getStatus(), null, null);
                }

            } else if (command instanceof InfoFile) {
                Path path = Paths.get(((InfoFile) command).getRemotePath());
                if (path.getNameCount() == 0) {     // only root in the path
                    ack = new InfoAck(StatusCodes.INCORRECT_NAME, 0, null, new UUID[0]);
                    IORoutines.sendSignal(conn, ack);
                    return;
                }

                InfoReturnValue returnValue = dispatcher.info(path);
                ack = new InfoAck(returnValue.getStatus(), returnValue.getFileSize(), returnValue.getAccessRights(), returnValue.getNodes());

            } else if (command instanceof CpFile) {
                Path fromPath = Paths.get(((CpFile) command).getFromPath());
                Path toPath = Paths.get(((CpFile) command).getToPath());
                if (fromPath.getNameCount() == 0 || toPath.getNameCount() == 0) {     // only root in the path
                    ack = new CpAck(StatusCodes.INCORRECT_NAME);
                    IORoutines.sendSignal(conn, ack);
                    return;
                }
                CpReturnValue returnValue = dispatcher.copy(fromPath, toPath);
                ack = new CpAck(returnValue.getStatus());

            } else if (command instanceof MvFile) {
                Path fromPath = Paths.get(((MvFile) command).getFromPath());
                Path toPath = Paths.get(((MvFile) command).getToPath());
                if (fromPath.getNameCount() == 0 || toPath.getNameCount() == 0) {     // only root in the path
                    ack = new MvAck(StatusCodes.INCORRECT_NAME);
                    IORoutines.sendSignal(conn, ack);
                    return;
                }

                MvReturnValue returnValue = dispatcher.move(fromPath, toPath);
                ack = new MvAck(returnValue.getStatus());

            } else if (command instanceof Cd) {
                Path path = Paths.get(((Cd) command).getRemotePath());

                if (dispatcher.directoryExists(path)) {
                    ack = new CdAck(StatusCodes.OK);
                } else {
                    ack = new CdAck(StatusCodes.FILE_OR_DIRECTORY_DOES_NOT_EXIST);
                }

            } else if (command instanceof Ls) {
                Path path = Paths.get(((Ls) command).getRemotePath());

                LsReturnValue returnValue = dispatcher.listDirectory(path);
                ack = new LsAck(returnValue.getStatus(), returnValue.getFolders(), returnValue.getFiles());

            } else if (command instanceof MkDir) {
                Path path = Paths.get(((MkDir) command).getRemotePath());

                MkDirReturnValue returnValue = dispatcher.makeDirectory(path);
                ack = new MkdirAck(returnValue.getStatus());
            } else if (command instanceof RmFile) {
                Path path = Paths.get(((RmFile) command).getRemotePath());
                if (path.getNameCount() == 0) {
                    ack = new RmAck(StatusCodes.INCORRECT_NAME);
                    IORoutines.sendSignal(conn, ack);
                    return;
                }

                if (dispatcher.folderExists(path)) {
                    ack = new RmAck(StatusCodes.CONFIRMATION_REQUIRED);
                    IORoutines.sendSignal(conn, ack);

                    command = IORoutines.receiveSignal(conn);
                    if (command instanceof RmConfirm) {
                        boolean isConfirmed = ((RmConfirm) command).isRemoveConfirmed();
                        System.out.println("IS confirmed: " + isConfirmed);
                        if (isConfirmed) {
                            dispatcher.removeFolder(path);
                        }
                        ack = new RmAck(StatusCodes.OK);
                    } else {
                        ack = new ErrorAck();
                    }
                } else if (dispatcher.fileExists(path)) {
                    dispatcher.removeFile(path);
                    ack = new RmAck(StatusCodes.OK);
                } else {
                    ack = new RmAck(StatusCodes.FILE_OR_DIRECTORY_DOES_NOT_EXIST);
                }
            }

        IORoutines.sendSignal(conn, ack);

        } catch (IOException ex) {
            System.err.println("Connection reset.");
        } catch (ClassNotFoundException | ClassCastException ex) {
            ex.printStackTrace();
            System.err.println("Unable to dispatch the command.");
        }
    }

    @Override
    public void run() {
        this.setUncaughtExceptionHandler((t, e) -> {
            try {
                server.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                System.err.println("IOException thrown while handling another exception " + ex);
            }
        });

        try {
            server = new ServerSocket(listeningPort);
            System.out.println("Socket has been bound to port " + listeningPort);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not bind a socket to port " + listeningPort);
        }

        try {
            serve();
        } catch (IOException e) {
            System.err.println("Server died.");
            e.printStackTrace();
        }
    }
}