package commons.commands.storage;

import java.net.InetAddress;
import java.util.Collection;
import java.util.UUID;

public class FileUpload extends StorageCommand {
	private UUID uuid;
	private long fileSize;
	private InetAddress[] replicaAddresses;

	public FileUpload(UUID uuid, long fileSize, Collection<InetAddress> replicaAddresses) {
		this.uuid = uuid;
		this.fileSize = fileSize;
		this.replicaAddresses = (InetAddress[]) replicaAddresses.toArray();
	}

	public FileUpload(UUID uuid, long fileSize, InetAddress... replicaAddresses) {
		this.uuid = uuid;
		this.fileSize = fileSize;
		this.replicaAddresses = replicaAddresses;
	}

	public UUID getUuid() {
		return uuid;
	}

	public InetAddress[] getReplicaAddresses() {
		return replicaAddresses;
	}

	public long getFileSize() {
		return fileSize;
	}
}
