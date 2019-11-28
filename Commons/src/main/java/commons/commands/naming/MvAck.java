package commons.commands.naming;

public class MvAck extends NamingCommandAck {
    public MvAck(int statusCode) {
        this.status = statusCode;
    }
}
