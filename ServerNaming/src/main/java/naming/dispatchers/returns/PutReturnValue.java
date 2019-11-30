package naming.dispatchers.returns;

import commons.StatusCodes;

import java.util.UUID;

public class PutReturnValue extends ReturnValue {
    UUID fileId;

    public PutReturnValue(StatusCodes.Code statusCode, UUID fileId) {
        super(statusCode);
        this.fileId = fileId;
    }

    public UUID getFileId() {
        return fileId;
    }
}
