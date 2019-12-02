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

    public List<Path> getAllFilesPaths() {
        return getFilesPathsRecursively(getRoot(), Paths.get("/"));
    }

    public List<Path> getFilesPathsRecursively(Folder directory, Path directoryPath) {
        List<Path> filesPaths = new LinkedList<>();
        appendFilesPaths(directory, directoryPath, filesPaths);

        return filesPaths;
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

    public boolean addFile(Path directoryPath, String fileName, int fileSize, String fileAccess, UUID fileId, boolean isTouched) {
        File file = new File(fileName, fileSize, fileAccess, fileId, isTouched);
        return addFile(directoryPath, file);
    }

    public boolean addFile(Path directoryPath, File file) {
        Folder directory = getFolder(directoryPath);
        if (directory != null && !directory.fileExists(file.getName()) && !directory.folderExists(file.getName())) {
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

    private void appendFilesPaths(Folder directory, Path directoryPath, List<Path> filesPaths) {
        for (Folder folder : directory.getFolders()) {
            Path path = Paths.get(directoryPath.toString(), folder.getName());
            appendFilesPaths(folder, path, filesPaths);
        }
        for (File file : directory.getFiles()) {
            filesPaths.add(Paths.get(directoryPath.toString(), file.getName()));
        }
    }
}