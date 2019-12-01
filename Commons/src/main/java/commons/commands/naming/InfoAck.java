package commons.commands.naming;

import commons.StatusCodes;

import java.nio.file.Path;
import java.util.Collection;
import java.util.UUID;

public class InfoAck extends NamingCommandAck {
    private long fileSize;
    private int accessRights;
    private UUID[] nodes;

    public InfoAck(int statusCode, long fileSize, int accessRights, UUID[] nodes) {
        super(statusCode);
        this.fileSize = fileSize;
        this.accessRights = accessRights;
        this.nodes = nodes;
    }

    public InfoAck(int statusCode, long fileSize, int accessRights, Collection<UUID> nodes) {
        super(statusCode);
        this.fileSize = fileSize;
        this.accessRights = accessRights;
        this.nodes = nodes.toArray(new UUID[0]);
    }

    public long getFileSize() {
        return fileSize;
    }

    public int getAccessRights() {
        return accessRights;
    }

    public UUID[] getNodes() {
        return nodes;
    }
}
