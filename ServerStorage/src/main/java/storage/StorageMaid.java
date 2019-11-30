package storage;

import java.io.File;
import java.util.*;

public class StorageMaid extends Thread {
	private Collection<UUID> uuids;

	public StorageMaid(Collection<UUID> uuids) {
		this.uuids = uuids;
	}

	public static Collection<UUID> getFiles() {
		File dataDir = new File(Main.dataPath);
		Set<UUID> files = new HashSet<>();
		for (File file : Objects.requireNonNull(dataDir.listFiles())) {
			files.add(UUID.fromString(file.getName()));
		}
		return files;
	}

	public static long getFileSize(UUID uuid) {
		File file = new File(Main.dataPath + uuid.toString());
		return file.length();
	}

	private void tidy() {
		Collection<UUID> localFiles = getFiles();
		localFiles.removeAll(uuids);

		for (UUID uuid : localFiles) {
			File file = new File(Main.dataPath + uuid.toString());
			if (!file.delete()) {
				System.err.println("MAID: Couldn't delete file " + uuid.toString());
			}
		}
	}

	@Override
	public void run() {
		tidy();
	}
}
