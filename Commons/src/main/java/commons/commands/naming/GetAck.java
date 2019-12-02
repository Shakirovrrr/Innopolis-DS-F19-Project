package commons.commands.naming;

import commons.StatusCodes;

import java.net.InetAddress;
import java.util.UUID;

public class GetAck extends NamingCommandAck {
    private InetAddress nodeAddress;
    private  UUID fileId;

    public GetAck(int statusCode, InetAddress nodeAddress, UUID fileId) {
        super(statusCode);
        this.nodeAddress = nodeAddress;
        this.fileId = fileId;
    }

    public InetAddress getNodeAddress() {
        return nodeAddress;
    }

    public UUID getFileId() {
        return fileId;
    }
}
