package storage;

import commons.Ports;

class Main {
	public static void main(String[] args) {
		System.out.println("Hello Storage!");

		ClientDispatcher dispatcher = new ClientDispatcher(Ports.PORT_STORAGE); // FIXME Replace port
		dispatcher.start();
	}
}