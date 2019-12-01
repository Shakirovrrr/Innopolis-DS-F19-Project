package naming.dispatchers;

import commons.StatusCodes;
import commons.commands.Command;
import commons.commands.general.ErrorAck;
import commons.commands.general.FileUploadAck;
import commons.commands.internal.FetchFiles;
import commons.commands.internal.FetchFilesAck;
import commons.commands.internal.Heartbeat;
import commons.commands.internal.RegisterNode;
import commons.routines.IORoutines;
import naming.dispatchers.returns.FetchFilesReturnValue;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class StorageServerDispatcher extends Thread {
    private int listeningPort;
    private ServerSocket server;
    private Dispatcher dispatcher;

    private Map<UUID, Long> serversHeartbeats;

    public StorageServerDispatcher(int listeningPort, Dispatcher dispatcher) {
        this.listeningPort = listeningPort;
        this.dispatcher = dispatcher;
        this.serversHeartbeats = new HashMap<>();

//        new Thread(() -> checkServers()).start();
    }

    private void checkServers() {
        while (true) {
            try {

                sleep(Constants.TIMER_SLEEP_TIME);
                for (UUID nodeId : serversHeartbeats.keySet()) {
                    if (serversHeartbeats.get(nodeId) + Constants.WAITING_TIME > new Date().getTime()) {
                        dispatcher.removeNode(nodeId);
                        serversHeartbeats.remove(nodeId);
                    } else {
                        break;
                    }
                }
            } catch (InterruptedException ex) {
                System.err.println("Thread interrupted.");
            }
        }
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

            if (command instanceof RegisterNode) {
                UUID nodeId = ((RegisterNode) command).getNodeId();
                List<UUID> files = Arrays.asList(((RegisterNode) command).getFiles());
                InetAddress publicAddress = ((RegisterNode) command).getPublicAddress();
                InetAddress localAddress = ((RegisterNode) command).getLocalAddress();

                dispatcher.registerNode(nodeId, files, publicAddress, localAddress);
                serversHeartbeats.put(nodeId, new Date().getTime());

            } else if (command instanceof FetchFiles) {
                UUID nodeId = ((FetchFiles) command).getNodeId();

                Command ack;
                if (!serversHeartbeats.containsKey(nodeId)) {
                    ack = new FetchFilesAck(StatusCodes.UNKNOWN_NODE);
                } else {
                    FetchFilesReturnValue returnValue = dispatcher.fetchFiles(nodeId);
                    ack = new FetchFilesAck(returnValue.getStatus(), returnValue.getExistedFiles(), returnValue.getFilesToDownload());
                }
                serversHeartbeats.replace(nodeId, new Date().getTime());
                IORoutines.sendSignal(conn, ack);

            } else if (command instanceof Heartbeat) {
                UUID nodeId = ((Heartbeat) command).getNodeId();
                serversHeartbeats.replace(nodeId, new Date().getTime());

            } else if (command instanceof FileUploadAck) {
                if (((FileUploadAck) command).getStatusCode() == StatusCodes.OK) {
                    dispatcher.addKeepingNode(((FileUploadAck) command).getFileUuid(), ((FileUploadAck) command).getStorageUuid());
                }

            } else {
                Command ack = new ErrorAck();
                IORoutines.sendSignal(conn, ack);
            }

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
            System.out.println("Server Storage Dispatcher. Uncaught exception has been got");
            e.printStackTrace();
        });

        try {
            server = new ServerSocket(listeningPort);
            System.out.println("Socket has been bound to port " + listeningPort);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not bind a socket to port " + listeningPort);
        }

        new Thread(this::checkServers).start();

        try {
            serve();
        } catch (IOException e) {
            System.err.println("Server died.");
            e.printStackTrace();
        }
    }
}
