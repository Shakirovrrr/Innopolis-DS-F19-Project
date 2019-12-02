package naming.dispatchers;

import commons.StatusCodes;
import commons.commands.internal.FetchFiles;
import commons.commands.internal.FetchFilesAck;
import naming.*;
import naming.dispatchers.returns.*;

import java.net.InetAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Dispatcher {
    private FileManager fileManager;
    private NodeStorage nodeStorage;
    private FileStorage fileStorage;

    private ReentrantLock mutex;

    public Dispatcher(FileManager fileManager, NodeStorage nodeStorage, FileStorage fileStorage) {
        this.fileManager = fileManager;
        this.nodeStorage = nodeStorage;
        this.fileStorage = fileStorage;

        this.mutex = new ReentrantLock();
    }

    public boolean init() {
        mutex.lock();
        fileManager = new FileManager();
        fileStorage = new FileStorage();
        for (Object node : nodeStorage) {
            ((Node) node).removeAllKeepingFiles();
        }
        Dumper.dumpTree(fileManager);
        mutex.unlock();

        return true;
    }

    public List<Node> getNodes() {
        mutex.lock();
        List<Node> nodes = new LinkedList<>(nodeStorage.getNodes());
        mutex.unlock();
        return nodes;
    }

    public TouchReturnValue touch(Path directoryPath, String fileName) {
        PutReturnValue returnValue = put(directoryPath, fileName, true, 0, Constants.DEFAULT_RIGHTS);
        Dumper.dumpTree(fileManager);
        return new TouchReturnValue(returnValue.getStatus());
    }

    public PutReturnValue put(Path directoryPath, String fileName, boolean isTouched, long fileSize, String fileRights) {
        UUID fileId = UUID.randomUUID();
        File file = new File(fileName, fileSize, fileRights, fileId, isTouched);

        PutReturnValue returnValue;
        mutex.lock();
        boolean fileAdded = fileManager.addFile(directoryPath, file);
        if (fileAdded) {
            fileStorage.addFile(fileId, Paths.get(directoryPath.toString(), fileName));
            returnValue = new PutReturnValue(StatusCodes.OK, fileId);
        } else {
            if (fileManager.getFolder(directoryPath) == null) {
               returnValue = new PutReturnValue(StatusCodes.FILE_OR_DIRECTORY_DOES_NOT_EXIST, null);
            } else {
                returnValue = new PutReturnValue(StatusCodes.FILE_OR_DIRECTORY_ALREADY_EXISTS, null);
            }
        }
        Dumper.dumpTree(fileManager);
        mutex.unlock();

        return returnValue;
    }

    public InfoReturnValue info(Path path) {
        InfoReturnValue returnValue;

        mutex.lock();
        File file = fileManager.getFile(path);
        if (file == null) {     // file or directory does not exist
            returnValue = new InfoReturnValue(StatusCodes.FILE_OR_DIRECTORY_DOES_NOT_EXIST);
        } else {
            List<UUID> nodeIds = new LinkedList<>();
            for (Node node : fileStorage.getFileNodes(file.getId())) {
                nodeIds.add(node.getNodeId());
            }
            returnValue = new InfoReturnValue(StatusCodes.OK, file.getSize(), file.getAccess(), nodeIds);
        }
        mutex.unlock();

        return returnValue;
    }

    public CpReturnValue copy(Path fromPath, Path toPath) {
        CpReturnValue returnValue;

        mutex.lock();
        File file = fileManager.getFile(fromPath);
        if (file == null) {
            returnValue = new CpReturnValue(StatusCodes.FILE_OR_DIRECTORY_DOES_NOT_EXIST);
            mutex.unlock();
            return returnValue;
        }

        Path directoryPath = toPath.getParent();
        String fileName = toPath.getFileName().toString();

        File newFile = new File(fileName, file.getSize(), file.getAccess(), file.getId(), file.getIsTouched());
        boolean fileAdded = fileManager.addFile(directoryPath, newFile);

        if (fileAdded) {
            fileStorage.addFile(file.getId(), toPath);
            returnValue = new CpReturnValue(StatusCodes.OK);
        } else {
            if (fileManager.getFolder(directoryPath) == null) {
                returnValue = new CpReturnValue(StatusCodes.FILE_OR_DIRECTORY_DOES_NOT_EXIST);
            } else {
                returnValue = new CpReturnValue(StatusCodes.FILE_OR_DIRECTORY_ALREADY_EXISTS);
            }
        }
        Dumper.dumpTree(fileManager);
        mutex.unlock();
        return returnValue;
    }

    public MvReturnValue move(Path fromPath, Path toPath) {
        MvReturnValue returnValue;

        mutex.lock();
        File file = fileManager.getFile(fromPath);
        if (file == null) {
            returnValue = new MvReturnValue(StatusCodes.FILE_OR_DIRECTORY_DOES_NOT_EXIST);
            mutex.unlock();
            return returnValue;
        }

        Path directoryPath = toPath.getParent();
        String fileName = toPath.getFileName().toString();

        File newFile = new File(fileName, file.getSize(), file.getAccess(), file.getId(), file.getIsTouched());
        boolean fileAdded = fileManager.addFile(directoryPath, newFile);

        if (fileAdded) {
            fileStorage.addFile(file.getId(), toPath);
            fileStorage.removeFilePath(file.getId(), fromPath);
            fileManager.removeFile(fromPath.getParent(), fromPath.getFileName().toString());

            returnValue = new MvReturnValue(StatusCodes.OK);
        } else {
            if (fileManager.getFolder(directoryPath) == null) {
                returnValue = new MvReturnValue(StatusCodes.FILE_OR_DIRECTORY_DOES_NOT_EXIST);
            } else {
                returnValue = new MvReturnValue(StatusCodes.FILE_OR_DIRECTORY_ALREADY_EXISTS);
            }
        }
        Dumper.dumpTree(fileManager);
        mutex.unlock();
        return returnValue;
    }

    public boolean directoryExists(Path path) {
        mutex.lock();
        boolean exists = fileManager.getFolder(path) != null;
        mutex.unlock();
        return exists;
    }

    public LsReturnValue listDirectory(Path path) {
        LsReturnValue returnValue;

        mutex.lock();
        Folder directory = fileManager.getFolder(path);
        if (directory == null) {
            returnValue = new LsReturnValue(StatusCodes.FILE_OR_DIRECTORY_DOES_NOT_EXIST, null, null);
        } else {
            List<String> folders = new LinkedList<>();
            for (Folder folder : directory.getFolders()) {
                folders.add(folder.getName());
            }
            List<String> files = new LinkedList<>();
            for (File file : directory.getFiles()) {
                files.add(file.getName());
            }
            returnValue = new LsReturnValue(StatusCodes.OK, folders, files);
        }
        mutex.unlock();

        return returnValue;
    }

    public MkDirReturnValue makeDirectory(Path path) {
        MkDirReturnValue returnValue;

        Path parentDirectoryPath = path.getParent();
        String folderName = path.getFileName().toString();

        mutex.lock();
        Folder parentDirectory = fileManager.getFolder(parentDirectoryPath);

        if (parentDirectory == null) {
            System.out.println("parent Directory is null " + parentDirectoryPath);
            returnValue = new MkDirReturnValue(StatusCodes.FILE_OR_DIRECTORY_DOES_NOT_EXIST);
        } else {
            System.out.println("Is ok");
            if (parentDirectory.folderExists(folderName)) {
                returnValue = new MkDirReturnValue(StatusCodes.FILE_OR_DIRECTORY_ALREADY_EXISTS);
            } else {
                Folder folder = new Folder(folderName);
                parentDirectory.addFolder(folder);

                returnValue = new MkDirReturnValue(StatusCodes.OK);
            }
        }
        Dumper.dumpTree(fileManager);
        mutex.unlock();
        return returnValue;
    }

    public GetReturnValue get(Path path) {
        GetReturnValue returnValue;

        mutex.lock();
        File file = fileManager.getFile(path);

        if (file == null) {
            returnValue = new GetReturnValue(StatusCodes.FILE_OR_DIRECTORY_DOES_NOT_EXIST,null, null);
        } else {
            if (file.getIsTouched()) {
                returnValue = new GetReturnValue(StatusCodes.IS_TOUCHED, null, null);
            } else {
                if (fileStorage.getFileNodes(file.getId()).size() == 0) {
                    returnValue = new GetReturnValue(StatusCodes.NO_NODES_AVAILABLE, null, null);
                } else {
                    Node node = new Node(fileStorage.getFileNodes(file.getId()).get(0));
                    returnValue = new GetReturnValue(StatusCodes.OK, node, file.getId());
                }
            }
        }
        mutex.unlock();

        return returnValue;
    }

    public void removeNode(UUID nodeId) {
        mutex.lock();
        Node node = nodeStorage.getNode(nodeId);
        Set<UUID> keepingFiles = node.getKeepingFiles();
        for (UUID fileId : keepingFiles) {
            fileStorage.removeFileNode(fileId, node);
        }
        nodeStorage.removeNode(nodeId);
        mutex.unlock();
    }

    public boolean folderExists(Path path) {
        return (fileManager.getFolder(path) != null);
    }

    public boolean fileExists(Path path) {
        return (fileManager.getFile(path) != null);
    }

    public void removeFolder(Path path) {
        mutex.lock();
        Folder folder = fileManager.getFolder(path);
        System.out.println("Remove Folder");
        if (folder != null) {
            List<Path> filesPaths = fileManager.getFilesPathsRecursively(folder, path);
            System.out.println("Folder is not null and contains " + filesPaths.size() + "objects");
            for (Path filePath : filesPaths) {
                System.out.println(filePath);
                remove(filePath);
            }
            if (!folder.getName().equals("root")) {
                Folder parentFolder = fileManager.getFolder(path.getParent());
                parentFolder.removeFolder(path.getFileName().toString());
            }
//            for (Folder innerFolder : folder.getFolders()) {
//                folder.removeFolder(innerFolder.getName());
//            }
        }
        Dumper.dumpTree(fileManager);
        mutex.unlock();
    }

    // TODO: Removes all copies of files, not only file from path
    public void removeFile(Path path) {
        mutex.lock();
        File file = fileManager.getFile(path);
        remove(path);
        Dumper.dumpTree(fileManager);
        mutex.unlock();
    }

    public void remove(Path path) {
        File file = fileManager.getFile(path);
        if (file != null) {
            UUID fileId = file.getId();
            fileStorage.removeFilePath(fileId, path);
            fileManager.removeFile(path.getParent(), path.getFileName().toString());
            if (!fileStorage.fileExists(fileId)) {
                for (Node node : nodeStorage.getNodes()) {
                    node.removeKeepingFile(fileId);
                }
            }
//            List<Path> directories = fileStorage.getFilePaths(file.getId());
//            for (Path directory : directories) {
//                Folder folder = fileManager.getFolder(directory);
//                folder.removeFile(file.getName());
//            }
//            List<Node> nodes = fileStorage.getFileNodes(file.getId());
//            for (Node node : nodes) {
//                node.removeKeepingFile(file.getId());
//            }
//            fileStorage.removeFile(file.getId());
        }
    }

    public void registerNode(UUID nodeId, List<UUID> fileIds, InetAddress publicAddress, InetAddress privateAddress) {
        Node node = new Node(nodeId, publicAddress, privateAddress);
        mutex.lock();
        nodeStorage.addNode(node);
        for (UUID fileId : fileIds) {
            if (fileStorage.fileExists(fileId)) {
                node.addKeepingFile(fileId);
                fileStorage.addFileNode(fileId, node);
            }
        }
        mutex.unlock();
    }

    public FetchFilesReturnValue fetchFiles(UUID nodeId) {
        mutex.lock();
        Node node = nodeStorage.getNode(nodeId);
        System.out.println("\n" + "Node: " + nodeId + " " + node.getKeepingFiles().size() + "\n" +
                "Node Storage: " + nodeStorage.getNodes().size() +
                "Storage Files: " + fileStorage.getFileIds().size());
        if (node == null) {
            return new FetchFilesReturnValue(StatusCodes.UNKNOWN_NODE, null, null);
        }

        Set<UUID> existedFiles = node.getKeepingFiles();
        Collection<FetchFilesAck.ToDownload> filesToDownload = new LinkedList<>();
        for (UUID fileId : fileStorage.getFileIds()) {
            if (!existedFiles.contains(fileId)) {
                List<Node> fileNodes = fileStorage.getFileNodes(fileId);
                if (fileNodes.size() > 0) {
                    FetchFilesAck.ToDownload toDownload = new FetchFilesAck.ToDownload(fileId, fileNodes.get(0).getPrivateIpAddress());
                    filesToDownload.add(toDownload);
                }
            }
        }
        mutex.unlock();

        return new FetchFilesReturnValue(StatusCodes.OK, existedFiles, filesToDownload);
    }

    public void addKeepingNode(UUID fileId, UUID nodeId) {
        mutex.lock();
        Node node = nodeStorage.getNode(nodeId);
        if (node != null && fileStorage.fileExists(fileId)) {
            node.addKeepingFile(fileId);
            fileStorage.addFileNode(fileId, node);
        }
        mutex.unlock();
    }
}
