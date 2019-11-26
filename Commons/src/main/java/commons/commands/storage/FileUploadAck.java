package commons.commands.storage;

@Deprecated(forRemoval = true)
public class FileUploadAck extends StorageCommand {
	// FUAck
	private int statusCode;

	public FileUploadAck(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
