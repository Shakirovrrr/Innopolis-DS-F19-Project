package commons.commands.naming;

import commons.StatusCodes;

public class ErrorAck extends NamingCommandAck {
    public ErrorAck() {
        this.status = StatusCodes.Code.UNKNOWN_COMMAND;
    }
}
