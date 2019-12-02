package storage;

import commons.commands.Command;
import commons.commands.storage.FileDownload;
import commons.commands.storage.FileUpload;
import commons.routines.IORoutines;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientDispatcher extends Thread {

	private int listeningPort;
	private ServerSocket server;

	ClientDispatcher(int listeningPort) {
		this.setDaemon(true);
		this.listeningPort = listeningPort;
	}

	private void serve() throws IOException {
		while (!this.isInterrupted()) {
			Socket conn = server.accept();
			System.out.println("DISPATCHER: Got connection from " + conn.getInetAddress());
			dispatch(conn);
		}
	}

	private void dispatch(Socket conn) {
		try {
			Command command = IORoutines.receiveSignal(conn);

			if (command instanceof FileDownload) {
				System.out.println("DISPATCHER: Asked to download file.");
				ClientSend sender = new ClientSend((FileDownload) command, conn);
				sender.start();
			} else if (command instanceof FileUpload) {
				System.out.println("DISPATCHER: Asked to upload file.");
				ClientReceive receiver = new ClientReceive((FileUpload) command, conn);
				receiver.start();
			}

		} catch (IOException ex) {
			ex.printStackTrace();
			System.err.println("DISPATCHER: Connection reset.");
		} catch (ClassNotFoundException | ClassCastException ex) {
			ex.printStackTrace();
			System.err.println("DISPATCHER: Unable to dispatch the command.");
		}
	}

	@Override
	public void run() {
		this.setUncaughtExceptionHandler((t, e) -> {
//			try {
//				server.close();
//			} catch (IOException ex) {
//				ex.printStackTrace();
//				System.err.println("IOException thrown while handling another exception " + ex);
//			}
			System.err.println("DISPATCHER: Ne padat'!");
		});

		try {
			server = new ServerSocket(listeningPort);
			System.out.println("DISPATCHER: Socket has been bound to port " + listeningPort);
		} catch (IOException e) {
			System.err.println("DISPATCHER: Could not bind a socket to port " + listeningPort);
		}

		try {
			serve();
		} catch (IOException e) {
			System.err.println("DISPATCHER: Server died.");
			e.printStackTrace();
			Main.die();
		}
	}
}
