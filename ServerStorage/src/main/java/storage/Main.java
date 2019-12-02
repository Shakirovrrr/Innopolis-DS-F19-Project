package storage;

import commons.Ports;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

class Main {
	static UUID nodeUuid;
	static String dataPath;
	static InetAddress namingAddress;
	static InetAddress localAddress;

	private static ClientDispatcher dispatcher;
	private static HeartbeatRunner heartbeatRunner;

	public static void main(String[] args) throws CouldNotRegisterException {
		System.out.println("Hello Storage!");

		dataPath = "./data/";
		nodeUuid = UUID.randomUUID();
		try {
			saveAddresses(args);
			Register.register();
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

	private static void saveAddresses(String[] args) throws UnknownHostException {
		if (args.length >= 1) {
			namingAddress = InetAddress.getByName(args[0]);
		}
		if (args.length >= 2) {
			localAddress = InetAddress.getByName(args[1]);
		}
	}
}