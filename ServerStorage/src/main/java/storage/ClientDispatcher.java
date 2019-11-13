package storage;

import java.io.IOException;
import java.net.ServerSocket;

public class ClientDispatcher extends Thread {

	private int listeningPort;
	private ServerSocket server;

	ClientDispatcher(int listeningPort) {
		this.listeningPort = listeningPort;
	}

	private void serve() throws IOException {
		while (true) {
			server.accept();
		}
	}

	@Override
	public void run() {
		this.setUncaughtExceptionHandler((t, e) -> {
			try {
				server.close();
			} catch (IOException ex) {
				System.err.println("IOException thrown while handling another exception " + ex);
				ex.printStackTrace();
			}
		});

		try {
			server = new ServerSocket(listeningPort);
			System.out.println("Socket has been bound to port " + listeningPort);
		} catch (IOException e) {
			System.err.println("Could not bind a socket to port " + listeningPort);
			e.printStackTrace();
		}

		try {
			serve();
		} catch (IOException e) {
			System.err.println("Server died.");
			e.printStackTrace();
		}
	}
}
