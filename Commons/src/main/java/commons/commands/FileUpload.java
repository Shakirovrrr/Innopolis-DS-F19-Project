package commons.commands;

import java.util.UUID;

public class FileUpload extends Command {
	private UUID uuid;

	public FileUpload(UUID uuid) {
		commandNum = 3;
		this.uuid = uuid;
	}

	public UUID getUuid() {
		return uuid;
	}
}
