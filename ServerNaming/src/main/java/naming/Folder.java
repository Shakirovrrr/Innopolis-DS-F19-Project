package naming;

import java.util.LinkedList;

public class Folder {
    private String name;
    private boolean isFolder;
    private LinkedList<Folder> folders;
    private LinkedList<File> files;

    public Folder(String name, boolean isFolder, LinkedList<Folder> folders, LinkedList<File> files) {
        assert isFolder;    // make sure that the structure preserves

        this.name = name;
        this.isFolder = true;
        this.folders = folders;
        this.files = files;
    }

    public String getName() {
        return name;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public LinkedList<Folder> getFolders() {
        return folders;
    }

    public LinkedList<File> getFiles() {
        return files;
    }
}
