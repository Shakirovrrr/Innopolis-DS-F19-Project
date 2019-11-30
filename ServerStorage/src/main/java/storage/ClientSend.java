package storage;

import commons.commands.storage.FileDownload;
import commons.routines.IORoutines;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClientSend extends Thread {
	private FileDownload command;
	private Socket conn;

	ClientSend(FileDownload command, Socket conn) {
		this.command = command;
		this.conn = conn;
	}

	private void sendFile() {
		FileInputStream fileIn;
		OutputStream sockOut;
		try {
			fileIn = new FileInputStream(command.getUuid().toString());
			sockOut = conn.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			IORoutines.transmit(fileIn, sockOut, 8192);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				fileIn.close();
				sockOut.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				System.err.println("IOException thrown while trying to close streams.");
			}
		}
	}

	@Override
	public void run() {
		sendFile();
	}
}
