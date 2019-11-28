package commons.commands.naming;

public class InitAck extends NamingCommandAck {
    public InitAck(int statusCode) {
        this.status = statusCode;
    }
}
