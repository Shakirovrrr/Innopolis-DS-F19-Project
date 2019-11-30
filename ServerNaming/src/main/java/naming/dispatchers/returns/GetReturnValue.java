package naming.dispatchers.returns;

import commons.StatusCodes;
import commons.commands.internal.NodePublicAddress;
import naming.Node;

import java.util.UUID;

public class GetReturnValue extends ReturnValue {
    private Node node;
    private UUID fileId;

    public GetReturnValue(StatusCodes.Code statusCode, Node node, UUID fileId) {
        super(statusCode);
        this.node = node;
        this.fileId = fileId;
    }

    public GetReturnValue(StatusCodes.Code statusCode, boolean isTouched, Node node, UUID fileId) {
        super(statusCode);
        this.node = node;
        this.fileId = fileId;
    }

    public Node getNode() {
        return node;
    }

    public UUID getFileId() {
        return fileId;
    }
}
