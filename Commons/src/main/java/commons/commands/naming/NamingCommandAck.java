package commons.commands.naming;

public abstract class NamingCommandAck extends NamingCommand {
    int status;

    public int getStatus() {
        return status;
    }
}
