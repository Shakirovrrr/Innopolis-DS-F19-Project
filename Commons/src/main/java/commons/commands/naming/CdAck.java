package commons.commands.naming;

import commons.StatusCodes;

public class CdAck extends NamingCommandAck {
    public CdAck(StatusCodes.Code statusCode) {
        this.status = statusCode;
    }
}
