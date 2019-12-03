package naming.dispatchers;

import commons.StatusCodes;
import commons.commands.Command;
import commons.commands.general.ErrorAck;
import commons.commands.general.FileUploadAck;
import commons.commands.internal.*;
import commons.routines.IORoutines;
import naming.dispatchers.returns.FetchFilesReturnValue;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class StorageServerDispatcher extends Thread {
	private int listeningPort;
	private ServerSocket server;
	private Dispatcher dispatcher;

	private ReentrantLock serversHeartbeatsLock;

	private Map<UUID, Long> serversHeartbeats;

	public StorageServerDispatcher(int listeningPort, Dispatcher dispatcher) {
		this.listeningPort = listeningPort;
		this.dispatcher = dispatcher;
		this.serversHeartbeats = new HashMap<>();
		this.serversHeartbeatsLock = new ReentrantLock();
	}

	private void checkServers() {
		while (true) {
			try {

				sleep(Constants.TIMER_SLEEP_TIME);
				serversHeartbeatsLock.lock();
				List<UUID> nodeIds = new LinkedList<>(serversHeartbeats.keySet());
				for (UUID nodeId : nodeIds) {
//                    System.out.println("Check Servers THREAD Timestamp " + serversHeartbeats.get(nodeId));
					if (new Date().getTime() - serversHeartbeats.get(nodeId) > Constants.WAITING_TIME) {
						System.out.println("Check Servers THREAD Deleting Node " + nodeId);
						System.out.println("Server " + nodeId + " removed");
						dispatcher.removeNode(nodeId);
						serversHeartbeats.remove(nodeId);
					}
				}
				serversHeartbeatsLock.unlock();
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
				System.out.println("Register Node " + nodeId);
				List<UUID> files = Arrays.asList(((RegisterNode) command).getFiles());
				InetAddress publicAddress = ((RegisterNode) command).getPublicAddress();
				InetAddress localAddress = ((RegisterNode) command).getLocalAddress();

				dispatcher.registerNode(nodeId, files, publicAddress, localAddress);

				serversHeartbeatsLock.lock();
				serversHeartbeats.put(nodeId, new Date().getTime());
				serversHeartbeatsLock.unlock();

				Command ack = new RegisterNodeAck(StatusCodes.OK);
				IORoutines.sendSignal(conn, ack);

			} else if (command instanceof FetchFiles) {
				UUID nodeId = ((FetchFiles) command).getNodeId();
				System.out.println("Fetch files command from " + nodeId + " get");

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
				System.out.println("Heartbeat from " + nodeId + " get");
			} else if (command instanceof FileUploadAck) {
				if (((FileUploadAck) command).getStatusCode() == StatusCodes.OK) {
					dispatcher.addKeepingNode(((FileUploadAck) command).getFileUuid(), ((FileUploadAck) command).getStorageUuid());
				}
				System.out.println(((FileUploadAck) command).getStorageUuid() + " acknowledges uploading of " + ((FileUploadAck) command).getFileUuid() + " file");

			} else {
				Command ack = new ErrorAck();
				IORoutines.sendSignal(conn, ack);
			}

		} catch (IOException ex) {
			System.err.println("Connection reset.");
			ex.printStackTrace();
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
