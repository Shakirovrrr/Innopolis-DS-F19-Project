package commons.commands.general;

import commons.commands.Command;

import java.util.UUID;

public class FileDownloadAck extends Command {
	private int statusCode;
	private UUID uuid;

	public FileDownloadAck(int statusCode, UUID uuid) {
		this.statusCode = statusCode;
		this.uuid = uuid;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public UUID getUuid() {
		return uuid;
	}
}
