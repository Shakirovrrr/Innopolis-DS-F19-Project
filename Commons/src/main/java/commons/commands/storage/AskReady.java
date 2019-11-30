package commons.commands.storage;

public class AskReady extends StorageCommand {
	private int fileSize;

	public AskReady(int fileSize) {
		this.fileSize = fileSize;
	}

	public int getFileSize() {
		return fileSize;
	}
}
