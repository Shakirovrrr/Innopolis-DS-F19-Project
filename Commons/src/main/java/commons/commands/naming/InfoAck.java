package commons.commands.naming;

import commons.StatusCodes;

import java.nio.file.Path;
import java.util.UUID;

public class InfoAck extends NamingCommandAck {
    private int fileSize;
    private int accessRights;

    public InfoAck(int statusCode, int fileSize, int accessRights) {
        super(statusCode);
        this.fileSize = fileSize;
        this.accessRights = accessRights;
    }

    public int getFileSize() {
        return fileSize;
    }

    public int getAccessRights() {
        return accessRights;
    }
}
