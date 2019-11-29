package commons.commands.naming;

import commons.StatusCodes;

public class RmAck extends NamingCommandAck {
    public RmAck(StatusCodes.Code statusCode) {
        this.status = statusCode;
    }
}