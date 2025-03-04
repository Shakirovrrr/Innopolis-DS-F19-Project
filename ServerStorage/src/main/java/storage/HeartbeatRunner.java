package storage;

import commons.Ports;
import commons.StatusCodes;
import commons.commands.internal.FetchFiles;
import commons.commands.internal.FetchFilesAck;
import commons.commands.internal.Heartbeat;
import commons.routines.IORoutines;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class HeartbeatRunner extends Thread {
	private void knockNaming() throws IOException {
		IORoutines.sendSignalOnce(Main.namingAddress,
				Ports.PORT_INTERNAL, new Heartbeat(Main.nodeUuid));
	}

	private void fetchFiles() throws IOException, ClassNotFoundException {
		Socket conn = new Socket(Main.namingAddress, Ports.PORT_INTERNAL);
		IORoutines.sendSignal(conn, new FetchFiles(Main.nodeUuid));
		FetchFilesAck ack = (FetchFilesAck) IORoutines.receiveSignal(conn);

		if (ack.getStatus() == StatusCodes.UNKNOWN_NODE) {
			System.err.println("HEARTBEAT: This server has been forgotten, re-registering...");
			Register.register();
		}

		if (ack.getExistedFiles() != null) {
			StorageMaid maid = new StorageMaid(Arrays.asList(ack.getExistedFiles()));
			maid.start();
		}

		if (ack.getFilesToDownload() != null) {
			Restorer restorer = new Restorer(ack);
			restorer.restore();
		}
	}

	private void heartbeat() throws InterruptedException {
		int knock = 0;
		int fails = 0;
		while (!this.isInterrupted()) {
			try {
				if (knock == 0) {
					fetchFiles();
				} else {
					knockNaming();
				}
				knock++;

				if (fails > 0) {
					fails = 0;
					System.out.println("HEARTBEAT: Connection restored.");
				}
			} catch (IOException ex) {
				fails++;
				System.err.printf("HEARTBEAT: Couldn't connect, trial %d of 5\n", fails);
				if (fails >= 5) {
					Main.die();
				}
			} catch (ClassCastException | ClassNotFoundException ex) {
				System.err.println("HEARTBEAT: Couldn't dispatch files acknowledgement.");
			}
			if (knock >= 6) {
				knock = 0;
			}

			Thread.sleep(5000);
		}
	}

	@Override
	public void run() {
		this.setUncaughtExceptionHandler((t, e) -> {
			System.err.println("HEARTBEAT: Ne padat'!");
			e.printStackTrace();
		});

		try {
			heartbeat();
		} catch (InterruptedException ex) {
			System.err.println("HEARTBEAT: Server died.");
		}
	}
}
