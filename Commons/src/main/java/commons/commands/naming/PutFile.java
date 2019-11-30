package commons.commands.naming;


public class PutFile extends NamingCommand {
    private String remotePath;

    public PutFile(String remotePath) {
        this.remotePath = remotePath;
    }

    public String getRemotePath() {
        return remotePath;
    }
}
