package commons.commands.storage;

import java.util.UUID;

public class FileDownload extends StorageCommand {
	private UUID uuid;

	public FileDownload(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getUuid() {
		return uuid;
	}
}
