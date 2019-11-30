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
		IORoutines.sendSignalOnce(address, Ports.PORT_INTERNAL, uploadAck);
	}

	private void notifyClientFail(UUID uuid) throws IOException {
		FileUploadAck uploadAck = new FileUploadAck(0, uuid);
		IORoutines.sendSignal(conn, uploadAck);
	}

	private OutputStream[] getReplicaOutputStreams(OutputStream fileOut, InetAddress[] addresses) throws IOException {
		OutputStream[] streams = new OutputStream[addresses.length + 1];
		FileUpload uploadCommand = new FileUpload(command.getUuid());
		streams[0] = fileOut;
		for (int i = 1; i <= addresses.length; i++) {
			Socket socket = new Socket(addresses[i], Ports.PORT_STORAGE);
			IORoutines.sendSignal(socket, uploadCommand);
			streams[i] = socket.getOutputStream();
		}
		return streams;
	}

	private void receiveFile() {
		InputStream sockIn;
		OutputStream sockOut;
		FileOutputStream fileOut;
		OutputStream[] sockReplica;

		try {
			fileOut = new FileOutputStream(Main.dataPath + command.getUuid().toString());
			sockIn = conn.getInputStream();
			sockOut = conn.getOutputStream();
			sockReplica = getReplicaOutputStreams(fileOut, command.getReplicaAddresses());
		} catch (IOException ex) {
			ex.printStackTrace();
			System.err.println("RECEIVE: Could not get/initialize streams for receiving files.");
			return;
		}

		try {
			System.out.println("RECEIVE: Receiving file " + command.getUuid().toString());
			IORoutines.transmitSplit(sockIn, sockReplica);
			System.out.println("RECEIVE: Done.");
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
				System.err.println("RECEIVE: IOException thrown while trying to close streams.");
			}
		}

		try {
			notifyNaming(0, command.getUuid());
			notifyClientFail(command.getUuid());
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("RECEIVE: Could not notify naming server nor client about fail.");
		}
	}

	@Override
	public void run() {
		receiveFile();
	}
}
