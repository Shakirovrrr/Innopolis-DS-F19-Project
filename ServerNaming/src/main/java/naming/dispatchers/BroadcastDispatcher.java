package naming.dispatchers;

import commons.Ports;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

public class BroadcastDispatcher extends Thread {
	public void broadcast() {
		while (true) {
			try {
				List<InetAddress> broadcastAddresses = listAllBroadcastAddresses();
				for (InetAddress address : broadcastAddresses) {
					System.out.println("Broadcast " + address);
					broadcastInterface(address);
				}
				Thread.sleep(Constants.BROADCAST_SLEEP_TIME);
			} catch (IOException | InterruptedException ex) {
				ex.printStackTrace();
			}

		}
	}

	private void broadcastInterface(InetAddress address) throws IOException {
		DatagramSocket socket = new DatagramSocket();
		socket.setBroadcast(true);

		String broadcastMessage = "NamingServer";
		byte[] buffer = broadcastMessage.getBytes();

		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, Ports.PORT_BROADCAST);
		socket.send(packet);
		socket.close();
	}

	private List<InetAddress> listAllBroadcastAddresses() throws SocketException {
		List<InetAddress> broadcastList = new ArrayList<>();
		Enumeration<NetworkInterface> interfaces
				= NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
			NetworkInterface networkInterface = interfaces.nextElement();

			if (networkInterface.isLoopback() || !networkInterface.isUp()) {
				continue;
			}

			networkInterface.getInterfaceAddresses().stream()
					.map(a -> a.getBroadcast())
					.filter(Objects::nonNull)
					.forEach(broadcastList::add);
		}
		return broadcastList;
	}

	@Override
	public void run() {
		this.setUncaughtExceptionHandler((t, e) -> {
			System.out.println("Broadcast Dispatcher. Uncaught exception has been got");
			e.printStackTrace();
		});
		broadcast();
//        new Thread(this::broadcast).start();
	}

}
