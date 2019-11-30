package commons.commands.naming;

import commons.StatusCodes;

public class InitAck extends NamingCommandAck {
    public InitAck(StatusCodes.Code statusCode) {
        this.status = statusCode;
    }
}
