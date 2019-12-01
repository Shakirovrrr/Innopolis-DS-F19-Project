package naming;

import java.nio.file.Path;
import java.util.*;

public class FileStorage {
    private Map<UUID, Set<Path>> filePaths;
    private Map<UUID, Set<Node>> fileToNodes;
    private Map<UUID, UUID> fileToMainNodeId;

    public FileStorage() {
        this.filePaths = new HashMap<>();
        this.fileToNodes = new HashMap<>();
        this.fileToMainNodeId = new HashMap<>();
    }

    public FileStorage(Map<UUID, Set<Path>> filePaths) {
        this.filePaths = filePaths;
        this.fileToNodes = new HashMap<>();
        this.fileToMainNodeId = new HashMap<>();
    }

    public FileStorage(Map<UUID, Set<Path>> filePaths, Map<UUID, Set<Node>> fileToNodes) {
        this.filePaths = filePaths;
        this.fileToNodes = fileToNodes;
        this.fileToMainNodeId = new HashMap<>();
    }

    public void removeFile(UUID fileId) {
        filePaths.remove(fileId);
        fileToNodes.remove(fileId);
        fileToMainNodeId.remove(fileId);
    }

    public void addFile(UUID fileId, Path filePath, UUID mainNodeId) {
        if (!filePaths.containsKey(fileId)) {
            Set<Path> paths = new HashSet<>();
            filePaths.put(fileId, paths);
            Set<Node> nodes = new HashSet<>();
            fileToNodes.put(fileId, nodes);
        }
        filePaths.get(fileId).add(filePath);
        fileToMainNodeId.put(fileId, mainNodeId);
    }

    public boolean addFileNode(UUID fileId, Node node) {
        if (fileToNodes.containsKey(fileId)) {
            fileToNodes.get(fileId).add(node);
            return true;
        }
        return false;
    }

    public void removeFileNode(UUID fileId, Node node) {
        if (fileToNodes.containsKey(fileId)) {
            fileToNodes.get(fileId).remove(node);
        }
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
                fileToNodes.remove(fileId);
            }
        }
    }

    public List<UUID> getFileIds() {
        return new LinkedList<UUID>(filePaths.keySet());
    }

    public List<Path> getFilePaths(UUID fileId) {
        return new LinkedList<Path>(filePaths.get(fileId));
    }

    public List<Node> getFileNodes(UUID fileId) {
        return new LinkedList<Node>(fileToNodes.get(fileId));
    }

    public boolean fileExists(UUID fileId) {
        return filePaths.containsKey(fileId);
    }

    public UUID getFileMainNode(UUID fileId) {
        return fileToMainNodeId.get(fileId);
    }

//    UUID fileId, Path filePath
}
