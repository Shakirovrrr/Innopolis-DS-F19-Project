package storage;

import commons.Ports;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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

	public static void main(String[] args) throws CouldNotStartException {
		System.out.println("Hello Storage!");

		dataPath = "./data/";
		nodeUuid = UUID.randomUUID();
		try {
			waitForNamingDatagram();
			Register.register();
		} catch (IOException e) {
			throw new CouldNotStartException("MAIN: Could not register at naming server.", e);
		} catch (NullPointerException e) {
			throw new CouldNotStartException("MAIN: Naming address was not provided.");
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
			System.out.println("MAIN: Using naming address " + namingAddress);
		}
		if (args.length >= 2) {
			localAddress = InetAddress.getByName(args[1]);
			System.out.println("MAIN: Using local address " + localAddress);
		}
	}

	private static void waitForNamingDatagram() throws CouldNotStartException {
		String message = "NamingServer";
		int bufferSize = message.length();
		try {
			DatagramSocket socket = new DatagramSocket(Ports.PORT_BROADCAST);
			byte[] buffer = new byte[bufferSize];
			DatagramPacket packet = new DatagramPacket(buffer, bufferSize);
			socket.receive(packet);
			String received = new String(packet.getData());
			if (received.equals(message)) {
				namingAddress = socket.getInetAddress();
				localAddress = socket.getLocalAddress();
				socket.close();
			}
		} catch (IOException e) {
			throw new CouldNotStartException("MAIN: Could not get naming address.");
		}
	}
}