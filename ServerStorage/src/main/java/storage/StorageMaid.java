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

		if (dataDir.listFiles() != null) {
			for (File file : Objects.requireNonNull(dataDir.listFiles())) {
				files.add(UUID.fromString(file.getName()));
			}
		}
		return files;
	}

	public static boolean ensureDataDirCreated() {
		File dataDir = new File(Main.dataPath);
		if (!dataDir.exists()) {
			return dataDir.mkdir();
		}
		return true;
	}

	public static long getFileSize(UUID uuid) {
		File file = new File(Main.dataPath + uuid.toString());
		return file.length();
	}

	public static boolean deleteFile(UUID uuid) {
		File file = new File(Main.dataPath + uuid.toString());
		return file.delete();
	}

	private void tidy() {
		Collection<UUID> localFiles = getFiles();
		localFiles.removeAll(uuids);

		if (localFiles.isEmpty()) return;

		System.out.println("MAID: Tidying...");
		for (UUID uuid : localFiles) {
			deleteFile(uuid);
		}
		System.out.println("MAID: Done tidying.");
	}

	@Override
	public void run() {
		tidy();
	}
}
