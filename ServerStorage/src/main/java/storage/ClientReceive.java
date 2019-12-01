package storage;

import commons.Ports;
import commons.StatusCodes;
import commons.commands.general.FileUploadAck;
import commons.commands.storage.ConfirmReady;
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
		FileUploadAck uploadAck = new FileUploadAck(status, command.getUuid(), Main.nodeUuid);
		IORoutines.sendSignalOnce(Main.namingAddress, Ports.PORT_INTERNAL, uploadAck);
	}

	private void notifyClient(int status) throws IOException {
		FileUploadAck uploadAck = new FileUploadAck(status, command.getUuid(), Main.nodeUuid);
		IORoutines.sendSignal(conn, uploadAck);
	}

	private OutputStream[] getReplicaOutputStreams(OutputStream fileOut, InetAddress[] addresses) throws IOException {
		OutputStream[] streams = new OutputStream[addresses.length + 1];
		FileUpload uploadCommand = new FileUpload(command.getUuid(), command.getFileSize());
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
			IORoutines.sendSignal(conn, new ConfirmReady());

			System.out.println("RECEIVE: Receiving file " + command.getUuid().toString());
			IORoutines.transmitNBytesSplit(command.getFileSize(), sockIn, sockReplica);
			System.out.println("RECEIVE: Done.");

			notifyClient(StatusCodes.OK);
		} catch (IOException ex) {
			try {
				fileOut.close();
				notifyNaming(StatusCodes.UPLOAD_FAILED);
				notifyClient(StatusCodes.UPLOAD_FAILED);
			} catch (IOException e) {
				System.err.println("RECEIVE: Could not notify naming server nor client about fail.");
			}
			ex.printStackTrace();
			System.err.println("RECEIVE: Connection lost.");
			if (StorageMaid.deleteFile(command.getUuid())) {
				System.out.println("MAID: Cleaned up uncompleted download.");
			} else {
				System.err.println("MAID: Couldn't clean up uncompleted download.");
			}
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
	}

	@Override
	public void run() {
		receiveFile();
	}
}
