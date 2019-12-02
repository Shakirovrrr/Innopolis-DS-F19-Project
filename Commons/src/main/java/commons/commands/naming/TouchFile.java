package commons.commands.naming;

public class TouchFile extends NamingCommand {
    private String newPath;

    public TouchFile(String newPath) {
        this.newPath = newPath;
    }

    public String getNewPath() {
        return newPath;
    }
}
