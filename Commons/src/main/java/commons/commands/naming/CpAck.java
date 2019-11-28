package commons.commands.naming;

import commons.StatusCodes;

public class CpAck extends NamingCommandAck {
    public CpAck(StatusCodes.Code statusCode) {
        this.status = statusCode;
    }
}
