package commons.commands.naming;

public class Ls extends NamingCommand {
    private String remotePath;

    public Ls(String remotePath) {
        this.remotePath = remotePath;
    }

    public String getRemotePath() {
        return remotePath;
    }
}
