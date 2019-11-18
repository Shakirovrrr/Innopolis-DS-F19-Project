package storage;

import commons.Ports;
import commons.commands.general.FileUploadAck;
import commons.commands.storage.FileUpload;
import commons.routines.IORoutines;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;

public class ClientReceive extends Thread {
	private FileUpload command;
	private Socket conn;

	ClientReceive(FileUpload command, Socket conn) {
		this.command = command;
		this.conn = conn;
	}

	private void notifyNaming(int status, UUID uuid) throws IOException {
		FileUploadAck uploadAck = new FileUploadAck(status, uuid);
		InetAddress address = InetAddress.getByName("192.168.1.2");
		IORoutines.sendSignalOnce(address, Ports.PORT_NAMING, uploadAck); // FIXME Replace port
	}

	private void notifyClient(int status, UUID uuid) throws IOException {
		FileUploadAck uploadAck = new FileUploadAck(status, uuid);
		IORoutines.sendSignal(conn, uploadAck);
	}

	private void receiveFile() {
		InputStream sockIn;
		OutputStream sockOut;
		FileOutputStream fileOut;

		try {
			fileOut = new FileOutputStream(command.getUuid().toString());
			sockIn = conn.getInputStream();
			sockOut = conn.getOutputStream();
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}

		try {
			IORoutines.transmit(sockIn, fileOut);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				notifyNaming(1, command.getUuid());
				fileOut.close();
				sockIn.close();
				sockOut.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				System.err.println("IOException thrown while trying to close streams.");
			}
		}

		try {
			notifyNaming(0, command.getUuid());
			notifyClient(0, command.getUuid());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		receiveFile();
	}
}
