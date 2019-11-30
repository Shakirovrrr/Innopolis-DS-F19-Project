package commons.commands.naming;

import commons.StatusCodes;

import java.util.Collection;
import java.util.UUID;

public class InfoAck extends NamingCommandAck {
    private long fileSize;
    private int accessRights;
    private Collection<UUID> nodes;

    public InfoAck(StatusCodes.Code statusCode, long fileSize, int accessRights, Collection<UUID> nodes) {
        this.status = statusCode;
        this.fileSize = fileSize;
        this.accessRights = accessRights;
        this.nodes = nodes;
    }

    public long getFileSize() {
        return fileSize;
    }

    public int getAccessRights() {
        return accessRights;
    }

    public Collection<UUID> getNodes() {
        return nodes;
    }
}
