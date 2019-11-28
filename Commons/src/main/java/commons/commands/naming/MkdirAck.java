package commons.commands.naming;

public class MkdirAck extends NamingCommandAck {
    public MkdirAck(int statusCode) {
        this.status = statusCode;
    }
}
