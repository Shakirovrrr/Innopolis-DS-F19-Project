package commons.commands.naming;

public class CpAck extends NamingCommandAck {
    public CpAck(int statusCode) {
        this.status = statusCode;
    }
}
