package commons.commands.naming;

import commons.StatusCodes;

public class TouchAck extends NamingCommandAck {
    public TouchAck(StatusCodes.Code statusCode) {
        this.status = statusCode;
    }
}
