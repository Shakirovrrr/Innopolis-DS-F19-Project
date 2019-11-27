package naming;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FileTree implements Iterable{
    private String username;
    private Folder root;

    FileTree(String username) {
        this.username = username;
        this.root = new Folder("root");
    }

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

    @Override
    public Iterator iterator() {
        List<File> files = new LinkedList<>();
        appendFiles(getRoot(), files);

        return files.iterator();
    }

    private void appendFiles(Folder directory, List<File> files) {
        for (Folder folder : directory.getFolders()) {
                appendFiles(folder, files);
        }
        files.addAll(directory.getFiles());
    }
}
