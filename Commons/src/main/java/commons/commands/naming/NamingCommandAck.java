package commons.commands.naming;

import commons.StatusCodes;

public abstract class NamingCommandAck extends NamingCommand {
    StatusCodes.Code status;

    public StatusCodes.Code getStatus() {
        return status;
    }
}
