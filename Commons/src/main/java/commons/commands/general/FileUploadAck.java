package commons.commands.general;

import commons.commands.Command;

import java.util.UUID;

public class FileUploadAck extends Command {
	// FUAck
	private int statusCode;
	private UUID fileUuid;
	private UUID storageUuid;

	public FileUploadAck(int statusCode, UUID fileUuid, UUID storageUuid) {
		this.statusCode = statusCode;
		this.fileUuid = fileUuid;
		this.storageUuid = storageUuid;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public UUID getStorageUuid() {
		return storageUuid;
	}

	public UUID getFileUuid() {
		return fileUuid;
	}
}
