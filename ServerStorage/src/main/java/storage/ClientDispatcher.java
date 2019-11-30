package storage;

import commons.commands.Command;
import commons.commands.storage.FileDownload;
import commons.commands.storage.FileUpload;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientDispatcher extends Thread {

	private int listeningPort;
	private ServerSocket server;

	ClientDispatcher(int listeningPort) {
		this.listeningPort = listeningPort;
	}

	private void serve() throws IOException {
		while (true) {
			Socket conn = server.accept();
//			dispatch(conn);
			new Thread(() -> dispatch(conn)).start();
		}
	}

	private void dispatch(Socket conn) {
		try {
			ObjectInputStream input = new ObjectInputStream(conn.getInputStream());
			Command command = (Command) input.readObject();

			if (command instanceof FileDownload) {
				ClientSend sender = new ClientSend((FileDownload) command, conn);
				sender.start();
			} else if (command instanceof FileUpload) {
				ClientReceive receiver = new ClientReceive((FileUpload) command, conn);
				receiver.start();
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
