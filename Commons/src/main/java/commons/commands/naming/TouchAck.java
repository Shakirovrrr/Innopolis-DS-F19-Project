package commons.commands.naming;

public class TouchAck extends NamingCommandAck {
    public TouchAck(int statusCode) {
        this.status = statusCode;
    }
}
