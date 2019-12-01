package naming.dispatchers.returns;

import java.util.Collection;
import java.util.UUID;

public class InfoReturnValue extends ReturnValue {
    long fileSize;
    int accessRights;
    Collection<UUID> nodes;

    public InfoReturnValue(int statusCode) {
        super(statusCode);
    }

    public InfoReturnValue(int statusCode, long fileSize, int accessRights, Collection<UUID> nodes) {
        super(statusCode);
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
