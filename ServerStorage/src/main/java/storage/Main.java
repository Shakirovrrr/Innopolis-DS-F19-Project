package storage;

import commons.Ports;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

class Main {
	static UUID nodeUuid;
	static String dataPath;
	static InetAddress namingAddress;

	public static void main(String[] args) throws CouldNotRegisterException {
		System.out.println("Hello Storage!");

		dataPath = "./data/";

		nodeUuid = UUID.randomUUID();
		try {
			namingAddress = InetAddress.getByName("192.168.0.1");
			Register.register(namingAddress);
		} catch (IOException e) {
			throw new CouldNotRegisterException("REGISTER: Could not register at naming server.", e);
		}

		ClientDispatcher dispatcher = new ClientDispatcher(Ports.PORT_STORAGE);
		dispatcher.start();
	}
}