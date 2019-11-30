package commons.commands.naming;

import commons.StatusCodes;

public class MvAck extends NamingCommandAck {
    public MvAck(StatusCodes.Code statusCode) {
        this.status = statusCode;
    }
}
