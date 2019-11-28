package commons.commands.naming;

public class CdAck extends NamingCommandAck {
    public CdAck(int statusCode) {
        this.status = statusCode;
    }
}
