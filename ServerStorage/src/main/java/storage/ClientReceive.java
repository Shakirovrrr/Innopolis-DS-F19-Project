package storage;

import commons.commands.FileUpload;
import commons.ioroutines.IORoutines;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientReceive extends Thread {
	private FileUpload command;
	private Socket conn;

	ClientReceive(FileUpload command, Socket conn) {
		this.command = command;
		this.conn = conn;
	}

	private void receiveFile() {
		InputStream sockIn;
		FileOutputStream fileOut;

		try {
			fileOut = new FileOutputStream(command.getUuid().toString());
			sockIn = conn.getInputStream();
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}

		try {
			IORoutines.transmit(sockIn, fileOut, 8192);

			// TODO Notify naming
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				fileOut.close();
				sockIn.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				System.err.println("IOException thrown while trying to close streams.");
			}
		}
	}

	@Override
	public void run() {
		receiveFile();
	}
}
