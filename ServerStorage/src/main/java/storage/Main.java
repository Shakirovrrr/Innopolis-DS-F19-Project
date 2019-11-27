package storage;

import commons.Ports;

import java.util.UUID;

class Main {
	static UUID nodeUuid;

	public static void main(String[] args) {
		System.out.println("Hello Storage!");

		nodeUuid = UUID.randomUUID();

		ClientDispatcher dispatcher = new ClientDispatcher(Ports.PORT_STORAGE);
		dispatcher.start();
	}
}