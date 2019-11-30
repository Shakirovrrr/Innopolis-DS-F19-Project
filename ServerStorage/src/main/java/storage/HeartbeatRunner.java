package storage;

import commons.Ports;
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
				Ports.PORT_INTERNAL, new Heartbeat());
	}

	private void fetchFiles() throws IOException, ClassNotFoundException {
		Socket conn = new Socket(Main.namingAddress, Ports.PORT_INTERNAL);
		IORoutines.sendSignal(conn, new FetchFiles());
		FetchFilesAck ack = (FetchFilesAck) IORoutines.receiveSignal(conn);

		StorageMaid maid = new StorageMaid(Arrays.asList(ack.getUuids()));
		maid.start();
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
				fails = 0;
				knock++;
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
		this.setUncaughtExceptionHandler((t, e) -> System.err.println("HEARTBEAT: Ne padat'!"));

		try {
			heartbeat();
		} catch (InterruptedException ex) {
			System.err.println("HEARTBEAT: Server died.");
		}
	}
}
