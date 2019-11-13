package naming;

public class FileTree {
    private String username;
    private Folder root;

    FileTree(String username, Folder root) {
        this.username = username;
        this.root = root;
    }

    public String getUsername() {
        return username;
    }

    public Folder getRoot() {
        return root;
    }
}
