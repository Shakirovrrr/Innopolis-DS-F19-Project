package commons.commands.storage;

public class AskReady extends StorageCommand {
	private long fileSize;

	public AskReady(long fileSize) {
		this.fileSize = fileSize;
	}

	public long getFileSize() {
		return fileSize;
	}
}
