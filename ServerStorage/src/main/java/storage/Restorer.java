package storage;

import commons.Ports;
import commons.StatusCodes;
import commons.commands.general.FileUploadAck;
import commons.commands.internal.FetchFilesAck;
import commons.commands.storage.ConfirmReady;
import commons.commands.storage.FileDownload;
import commons.routines.IORoutines;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Restorer {
	private FetchFilesAck fetchAck;

	public Restorer(FetchFilesAck fetchAck) {
		this.fetchAck = fetchAck;
	}

	public void restore() {
		if (fetchAck.getFilesToDownload() == null) return;

		for (FetchFilesAck.ToDownload toDownload : fetchAck.getFilesToDownload()) {
			new Thread(() -> restoreJob(toDownload)).start();
		}
	}

	private void restoreJob(FetchFilesAck.ToDownload toDownload) {
		FileDownload request = new FileDownload(toDownload.getFileUuid());
		Socket conn;
		InputStream sockIn;
		FileOutputStream fileOut;

		try {
			conn = new Socket(toDownload.getNodeAddress(), Ports.PORT_STORAGE);
		} catch (IOException e) {
			return;
		}

		try {
			fileOut = new FileOutputStream(Main.dataPath + toDownload.getFileUuid().toString());
			sockIn = conn.getInputStream();
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

			System.out.println("RESTORER: Downloading file " + toDownload.getFileUuid());
			IORoutines.transmit(sockIn, fileOut);
			System.out.println("RESTORER: Restored file " + toDownload.getFileUuid());

			IORoutines.sendSignalOnce(Main.namingAddress, Ports.PORT_INTERNAL,
					new FileUploadAck(StatusCodes.OK, toDownload.getFileUuid(), Main.nodeUuid));
		} catch (IOException | ClassNotFoundException | ClassCastException ignored) {
		} finally {
			try {
				fileOut.close();
				sockIn.close();
			} catch (IOException ignored) {
			}
		}
	}
}
