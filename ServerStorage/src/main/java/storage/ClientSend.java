package storage;

import commons.commands.storage.AskReady;
import commons.commands.storage.ConfirmReady;
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
			fileIn = new FileInputStream(Main.dataPath + command.getUuid().toString());
			sockOut = conn.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			IORoutines.sendSignal(conn, new AskReady());

			ConfirmReady confirm = (ConfirmReady) IORoutines.receiveSignal(conn);

			if (confirm.isAgree()) {
				System.out.println("SEND: Sending file " + command.getUuid().toString());
				IORoutines.transmit(fileIn, sockOut);
				System.out.println("SEND: Done.");
			}
		} catch (IOException ex) {
			System.err.println("Connection failed.");
		} catch (ClassNotFoundException | ClassCastException ex) {
			System.err.println("SEND: Bad confirm command.");
		} finally {
			try {
				fileIn.close();
				sockOut.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				System.err.println("SEND: IOException thrown while trying to close streams.");
			}
		}
	}

	@Override
	public void run() {
		sendFile();
	}
}
