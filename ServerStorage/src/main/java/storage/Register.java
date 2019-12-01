package storage;

import commons.Ports;
import commons.commands.internal.RegisterNode;
import commons.commands.internal.RegisterNodeAck;
import commons.routines.IORoutines;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.UUID;

class Register {
	static void register(InetAddress namingAddress) throws IOException {
		register(namingAddress, new URL("http://checkip.amazonaws.com"));
	}

	static void register(InetAddress namingAddress, URL ipCheckSite) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(ipCheckSite.openStream()));
		String publicIp = reader.readLine();
		reader.close();

		UUID[] localFiles = StorageMaid.getFiles().toArray(new UUID[0]);

		RegisterNode registerNodeCmd = new RegisterNode(Main.nodeUuid, localFiles,
				InetAddress.getByName(publicIp), InetAddress.getLocalHost());

		Socket conn = new Socket(namingAddress, Ports.PORT_INTERNAL);
		IORoutines.sendSignal(conn, registerNodeCmd);
		try {
			RegisterNodeAck ack = (RegisterNodeAck) IORoutines.receiveSignal(conn);
		} catch (ClassNotFoundException | ClassCastException e) {
			throw new IOException();
		}
	}
}
