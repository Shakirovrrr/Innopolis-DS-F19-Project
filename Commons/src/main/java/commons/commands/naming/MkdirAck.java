package commons.commands.naming;

import commons.StatusCodes;

public class MkdirAck extends NamingCommandAck {
    public MkdirAck(StatusCodes.Code statusCode) {
        this.status = statusCode;
    }
}
