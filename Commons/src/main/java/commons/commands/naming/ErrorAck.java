package commons.commands.naming;

import commons.StatusCodes;

@Deprecated(forRemoval = true)
public class ErrorAck extends NamingCommandAck {
    public ErrorAck() {
        this.status = StatusCodes.Code.UNKNOWN_COMMAND;
    }
}
