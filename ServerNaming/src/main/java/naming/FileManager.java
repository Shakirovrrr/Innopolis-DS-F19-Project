package naming;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileManager {
    private Folder root;

    public FileManager() {
        this.root = new Folder("root");
    }

    public FileManager(Folder root) {
        this.root = root;
    }

    public Folder getRoot() {
        return root;
    }

    public List<File> getAllFiles() {
        List<File> files = new LinkedList<>();
        appendFiles(getRoot(), files);

        return files;
    }

    public List<File> getFilesRecursively(Folder directory) {
        List<File> files = new LinkedList<>();
        appendFiles(directory, files);

        return files;
    }

    public Folder getFolder(Path path) {
        Folder currentFolder = getRoot();
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

    public boolean addFile(Path directoryPath, String fileName, int fileSize, int fileAccess, UUID fileId, boolean isTouched, Set<UUID> nodes) {
        File file = new File(fileName, fileSize, fileAccess, fileId, isTouched, nodes);
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

    public boolean removeFile(Path directoryPath, String fileName) {
        Folder directory = getFolder(directoryPath);
        if (directory != null) {
            return directory.removeFile(fileName);
        }
        return false;
    }

    public boolean removeFolder(Path directoryPath, String folderName) {
        Folder directory = getFolder(directoryPath);
        if (directory != null) {
            return directory.removeFolder(folderName);
        }
        return false;
    }

    private void appendFiles(Folder directory, List<File> files) {
        for (Folder folder : directory.getFolders()) {
            appendFiles(folder, files);
        }
        files.addAll(directory.getFiles());
    }
}