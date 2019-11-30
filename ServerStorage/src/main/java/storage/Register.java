package storage;

import commons.commands.internal.RegisterNode;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

class Register {
	static void register(InetAddress namingAddress) throws IOException {
		URL ipChecker = null;
		try {
			ipChecker = new URL("http://checkip.amazonaws.com");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		assert ipChecker != null;

		BufferedReader reader = new BufferedReader(new InputStreamReader(ipChecker.openStream()));
		String publicIp = reader.readLine();
		reader.close();

		RegisterNode registerNodeCmd = new RegisterNode(Main.nodeUuid, getFiles(),
				InetAddress.getByName(publicIp), InetAddress.getLocalHost());

//		IORoutines.sendSignalOnce(namingAddress, Ports.PORT_INTERNAL, registerNodeCmd);
	}

	private static UUID[] getFiles() {
		File dataDir = new File("./data");
		List<UUID> files = new LinkedList<>();
		for (File file : Objects.requireNonNull(dataDir.listFiles())) {
			files.add(UUID.fromString(file.getName()));
		}

		return files.toArray(UUID[]::new);
	}
}
