package storage;

import commons.Ports;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

class Main {
	static UUID nodeUuid;
	static String dataPath;
	static InetAddress namingAddress;

	private static ClientDispatcher dispatcher;
	private static HeartbeatRunner heartbeatRunner;

	public static void main(String[] args) throws CouldNotRegisterException {
		System.out.println("Hello Storage!");

		dataPath = "./data/";
		nodeUuid = UUID.randomUUID();
		try {
			namingAddress = InetAddress.getByName("10.91.51.171");
			Register.register(namingAddress);
		} catch (IOException e) {
			throw new CouldNotRegisterException("MAIN: Could not register at naming server.", e);
		}

		heartbeatRunner = new HeartbeatRunner();
		heartbeatRunner.start();

		dispatcher = new ClientDispatcher(Ports.PORT_STORAGE);
		dispatcher.start();
	}

	public static void die() {
		dispatcher.interrupt();
		heartbeatRunner.interrupt();
	}
}