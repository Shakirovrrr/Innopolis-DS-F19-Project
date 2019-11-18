package commons.commands;

import java.util.UUID;

public class FileDownload extends Command {
	private UUID uuid;

	public FileDownload(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getUuid() {
		return uuid;
	}
}
