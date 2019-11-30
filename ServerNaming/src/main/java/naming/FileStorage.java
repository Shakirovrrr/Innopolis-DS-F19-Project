package naming;

import java.nio.file.Path;
import java.util.*;

public class FileStorage {
    private Map<UUID, Set<Path>> filePaths;

    public FileStorage() {
        this.filePaths = new HashMap<>();
    }

    public FileStorage(Map<UUID, Set<Path>> filePaths) {
        this.filePaths = filePaths;
    }

    public void removeFile(UUID fileId) {
        filePaths.remove(fileId);
    }

    public void addFile(UUID fileId, Path filePath) {
        if (!filePaths.containsKey(fileId)) {
            Set<Path> paths = new HashSet<>();
            filePaths.put(fileId, paths);
        }
        filePaths.get(fileId).add(filePath);
    }

//    public void addFilePath(UUID fileId, Path filePath) {
//        if (filePaths.containsKey(fileId)) {
//            filePaths.get(fileId).add(filePath);
//        }
//    }

    public void removeFilePath(UUID fileId, Path filePath) {
        if (filePaths.containsKey(fileId)) {
            filePaths.get(fileId).remove(filePath);
            if (filePaths.get(fileId).size() == 0) {
                filePaths.remove(fileId);
            }
        }
    }

    public List<UUID> getFileIds() {
        return new LinkedList<UUID>(filePaths.keySet());
    }

    public List<Path> getFilePaths(UUID fileId) {
        return new LinkedList<Path>(filePaths.get(fileId));
    }

//    UUID fileId, Path filePath
}
