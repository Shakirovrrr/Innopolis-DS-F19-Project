package naming;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

public class FileManager {
    private FileTree fileTree;

    public FileManager() {
        this.fileTree = new FileTree("uniuser");
    }

    public FileManager(Folder root) {
        this.fileTree = new FileTree("uniuser", root);
    }

    public FileManager(String username) {
        this.fileTree = new FileTree(username);
    }

    public FileManager(String username, Folder root) {
        this.fileTree  = new FileTree(username, root);
    }

    public FileManager(FileTree fileTree) {
        this.fileTree = fileTree;
    }

    public FileTree getFileTree() {
        return fileTree;
    }

    public Folder getFolder(Path path) {
        Folder currentFolder = fileTree.getRoot();
        for (int i = 0; i < path.getNameCount(); i++) {
            String folderName = path.getName(i).toString();
            if (currentFolder.folderExists(folderName)) {
                currentFolder = currentFolder.getFolder(folderName);
            } else {
                currentFolder = null;
                break;
            }
        }
        return currentFolder;
    }

    public File getFile(Path path) {
        // not only '/' provided as a path
        if (path.getNameCount() > 0) {
            String fileName = path.getFileName().toString();

            Folder directory;
            if (path.getNameCount() > 1) {
                directory = getFolder(path.subpath(0, path.getNameCount()-1));
            } else {    // the file is located in the root directory
                directory = getFolder(Paths.get("/"));
            }

            if (directory != null && directory.fileExists(fileName)) {
                return directory.getFile(fileName);
            }
        }

        return null;
    }

    public boolean addFile(Path directoryPath, String fileName, int fileSize, int fileAccess, UUID fileId, Set<Node> nodes) {
        File file = new File(fileName, fileSize, fileAccess, fileId, nodes);
        return addFile(directoryPath, file);
    }

    public boolean addFile(Path directoryPath, File file) {
        Folder directory = getFolder(directoryPath);
        if (directory != null && directory.fileExists(file.getName()) == false) {
            directory.addFile(file);
            return true;
        }
        return false;
    }
}
