package storage;

import commons.Ports;
import commons.commands.storage.ConfirmReady;
import commons.commands.storage.FileUpload;
import commons.routines.IORoutines;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Replicator {
	private FileUpload command;

	public Replicator(FileUpload command) {
		this.command = command;
	}

	public void startReplica() {
		for (InetAddress replicaAddress : this.command.getReplicaAddresses()) {
			new Thread(() -> replicaJob(replicaAddress)).start();
		}
	}

	private void replicaJob(InetAddress address) {
		FileUpload request = new FileUpload(command.getUuid(), command.getFileSize());
		Socket conn;
		FileInputStream fileIn;
		OutputStream sockOut;

		try {
			conn = new Socket(address, Ports.PORT_STORAGE);
		} catch (IOException e) {
			return;
		}

		try {
			fileIn = new FileInputStream(Main.dataPath + command.getUuid().toString());
			sockOut = conn.getOutputStream();
		} catch (IOException e) {
			try {
				conn.close();
			} catch (IOException ignored) {
			}
			return;
		}

		try {
			IORoutines.sendSignal(conn, request);
			ConfirmReady ready = (ConfirmReady) IORoutines.receiveSignal(conn);
			if (ready.isAgree()) {
				IORoutines.transmit(fileIn, sockOut);
			}
			IORoutines.receiveSignal(conn);
		} catch (IOException | ClassNotFoundException | ClassCastException ignored) {
		} finally {
			try {
				fileIn.close();
				conn.close();
			} catch (IOException ignored) {
			}
		}
	}
}
