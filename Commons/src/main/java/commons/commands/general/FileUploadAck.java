package commons.commands.general;

import commons.StatusCodes;
import commons.commands.Command;

import java.util.UUID;

public class FileUploadAck extends Command {
    // FUAck
    private int statusCode;
    private UUID uuid;
    StatusCodes statusCodes = new StatusCodes();


    public FileUploadAck(int statusCode, UUID uuid) {
        this.statusCode = statusCode;
        this.uuid = uuid;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusStr() {
        return this.statusCodes.getStatusCode(statusCode);
    }

    public UUID getUuid() {
        return uuid;
    }
}
