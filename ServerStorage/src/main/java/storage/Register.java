package storage;

import commons.Ports;
import commons.commands.internal.RegisterNode;
import commons.routines.IORoutines;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
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

		UUID[] localFiles = FileMaid.getFiles().toArray(new UUID[0]);

		RegisterNode registerNodeCmd = new RegisterNode(Main.nodeUuid, localFiles,
				InetAddress.getByName(publicIp), InetAddress.getLocalHost());

		IORoutines.sendSignalOnce(namingAddress, Ports.PORT_INTERNAL, registerNodeCmd);
	}
}
