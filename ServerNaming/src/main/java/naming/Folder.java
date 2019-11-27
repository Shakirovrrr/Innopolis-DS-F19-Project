package naming;

import java.util.LinkedList;
import java.util.List;

public class Folder {
    private String name;
    private List<Folder> folders;
    private List<File> files;

    public Folder(String name) {
        this.name = name;
        this.folders = new LinkedList<>();
        this.files = new LinkedList<>();
    }

    public Folder(String name, List<Folder> folders, List<File> files) {
        this.name = name;
        this.folders = folders;
        this.files = files;
    }

    public String getName() {
        return name;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public List<File> getFiles() {
        return files;
    }
}
