package storage;

import commons.Ports;
import commons.StatusCodes;
import commons.commands.general.FileUploadAck;
import commons.commands.storage.FileUpload;
import commons.routines.IORoutines;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientReceive extends Thread {
	private FileUpload command;
	private Socket conn;

	ClientReceive(FileUpload command, Socket conn) {
		this.command = command;
		this.conn = conn;
	}

	private void notifyNaming(int status) throws IOException {
		FileUploadAck uploadAck = new FileUploadAck(status, command.getUuid());
		IORoutines.sendSignalOnce(Main.namingAddress, Ports.PORT_INTERNAL, uploadAck);
	}

	private void notifyClient(int status) throws IOException {
		FileUploadAck uploadAck = new FileUploadAck(status, command.getUuid());
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
			conn.setSoTimeout(10000);
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

			FileUploadAck ack = new FileUploadAck(StatusCodes.OK, command.getUuid());
			IORoutines.sendSignal(conn, ack);
		} catch (IOException ex) {
			System.err.println("RECEIVE: Connection lost.");
		} finally {
			try {
				fileOut.close();
				notifyNaming(StatusCodes.OK);
				sockIn.close();
				sockOut.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				System.err.println("RECEIVE: IOException thrown while trying to close streams.");
			}
		}

		try {
			notifyNaming(StatusCodes.FILE_ALREADY_EXISTS); // TODO Correct code
			notifyClient(StatusCodes.FILE_ALREADY_EXISTS);
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
