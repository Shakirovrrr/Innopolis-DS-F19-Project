package commons.commands.naming;

import commons.StatusCodes;

import java.nio.file.Path;
import java.util.UUID;

public class InfoAck extends NamingCommandAck {
    private Path path;
    private int fileSize;
    private int accessRights;

    public InfoAck(StatusCodes.Code statusCode, Path path, int fileSize, int accessRights) {
        this.status = statusCode;
        this.path = path;
        this.fileSize = fileSize;
        this.accessRights = accessRights;
    }

    public Path getPath() {
        return path;
    }

    public int getFileSize() {
        return fileSize;
    }

    public int getAccessRights() {
        return accessRights;
    }
}
