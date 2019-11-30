package commons.commands.naming;

public class CpFile extends NamingCommand {
    private String fromPath;
    private String toPath;

    public CpFile(String fromPath, String toPath) {
        this.fromPath = fromPath;
        this.toPath = toPath;
    }

    public String getFromPath() {
        return fromPath;
    }

    public String getToPath() {
        return toPath;
    }
}
