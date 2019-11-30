package commons.commands.storage;

import java.net.InetAddress;
import java.util.Collection;
import java.util.UUID;

public class FileUpload extends StorageCommand {
	private UUID uuid;
	private InetAddress[] replicaAddresses;

	public FileUpload(UUID uuid, Collection<InetAddress> replicaAddresses) {
		this.uuid = uuid;
		this.replicaAddresses = replicaAddresses.toArray(InetAddress[]::new);
	}

	public FileUpload(UUID uuid, InetAddress... replicaAddresses) {
		this.uuid = uuid;
		this.replicaAddresses = replicaAddresses;
	}

	public UUID getUuid() {
		return uuid;
	}

	public InetAddress[] getReplicaAddresses() {
		return replicaAddresses;
	}
}
