package commons.commands.general;

import commons.StatusCodes;
import commons.commands.Command;

public class ErrorAck extends Command {
    private int status;

    public ErrorAck() {
        this.status = StatusCodes.UNKNOWN_COMMAND;
    }

    public int getStatus() {
        return status;
    }
}
