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
			sockOut = conn.getOutputStream();
			fileIn = new FileInputStream(Main.dataPath + command.getUuid().toString());
		} catch (IOException e) {
			try {
				conn.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return;
		}

		try {
			long fileSize = StorageMaid.getFileSize(command.getUuid());
			IORoutines.sendSignal(conn, new AskReady(fileSize));

			ConfirmReady confirm = (ConfirmReady) IORoutines.receiveSignal(conn);

			if (confirm.isAgree()) {
				System.out.println("SEND: Sending file " + command.getUuid().toString());
				IORoutines.transmit(fileIn, sockOut);
				System.out.println("SEND: Done.");
			}
		} catch (IOException ex) {
			System.err.println("SEND: Connection lost.");
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
