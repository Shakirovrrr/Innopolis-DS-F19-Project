package naming.dispatchers.returns;

import java.util.UUID;

public class PutReturnValue extends ReturnValue {
	UUID fileId;

	public PutReturnValue(int statusCode, UUID fileId) {
		super(statusCode);
		this.fileId = fileId;
	}

	public UUID getFileId() {
		return fileId;
	}
}
