package naming.dispatchers;

import commons.StatusCodes;
import naming.*;
import naming.dispatchers.returns.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Dispatcher {
    private FileManager fileManager;
    private NodeStorage nodeStorage;
    private FileStorage fileStorage;

    public Dispatcher(FileManager fileManager, NodeStorage nodeStorage, FileStorage fileStorage) {
        this.fileManager = fileManager;
        this.nodeStorage = nodeStorage;
        this.fileStorage = fileStorage;
    }

    public boolean init() {
        fileManager = new FileManager();
        fileStorage = new FileStorage();
        for (Object node : nodeStorage) {
            ((Node) node).removeAllKeepingFiles();
        }

        return true;
    }

    public List<Node> getNodes() {
        return  nodeStorage.getNodes();
    }

    public PutReturnValue put(Path directoryPath, String fileName, boolean isTouched) {
        UUID fileId = UUID.randomUUID();
        File file = new File(fileName, fileId, isTouched);

        PutReturnValue returnValue;
        boolean fileAdded = fileManager.addFile(directoryPath, file);
        if (fileAdded) {
            fileStorage.addFile(fileId, Paths.get(directoryPath.toString() + fileName));
            returnValue = new PutReturnValue(StatusCodes.Code.OK, fileId);
        } else {
            if (fileManager.getFolder(directoryPath) == null) {
               returnValue = new PutReturnValue(StatusCodes.Code.FILE_OR_DIRECTORY_DOES_NOT_EXIST, null);
            } else {
                returnValue = new PutReturnValue(StatusCodes.Code.FILE_OR_DIRECTORY_ALREADY_EXISTS, null);
            }
        }

        return returnValue;
    }

    public InfoReturnValue info(Path path) {
        InfoReturnValue returnValue;

        File file = fileManager.getFile(path);
        if (file == null) {     // file or directory does not exist
            returnValue = new InfoReturnValue(StatusCodes.Code.FILE_OR_DIRECTORY_DOES_NOT_EXIST);
        } else {
            returnValue = new InfoReturnValue(StatusCodes.Code.OK, file.getSize(), file.getAccess(), file.getNodes());
        }

        return returnValue;
    }

    public CpReturnValue copy(Path fromPath, Path toPath) {
        CpReturnValue returnValue;

        File file = fileManager.getFile(fromPath);
        if (file == null) {
            returnValue = new CpReturnValue(StatusCodes.Code.FILE_OR_DIRECTORY_DOES_NOT_EXIST);
            return returnValue;
        }

        Path directoryPath = toPath.getParent();
        String fileName = toPath.getFileName().toString();

        File newFile = new File(fileName, file.getSize(), file.getAccess(), file.getId(), file.getIsTouched());
        boolean fileAdded = fileManager.addFile(directoryPath, newFile);

        if (fileAdded) {
            fileStorage.addFile(file.getId(), toPath);
            returnValue = new CpReturnValue(StatusCodes.Code.OK);
        } else {
            if (fileManager.getFolder(directoryPath) == null) {
                returnValue = new CpReturnValue(StatusCodes.Code.FILE_OR_DIRECTORY_DOES_NOT_EXIST);
            } else {
                returnValue = new CpReturnValue(StatusCodes.Code.FILE_OR_DIRECTORY_ALREADY_EXISTS);
            }
        }
        return returnValue;
    }

    public MvReturnValue move(Path fromPath, Path toPath) {
        MvReturnValue returnValue;

        File file = fileManager.getFile(fromPath);
        if (file == null) {
            returnValue = new MvReturnValue(StatusCodes.Code.FILE_OR_DIRECTORY_DOES_NOT_EXIST);
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

            returnValue = new MvReturnValue(StatusCodes.Code.OK);
        } else {
            if (fileManager.getFolder(directoryPath) == null) {
                returnValue = new MvReturnValue(StatusCodes.Code.FILE_OR_DIRECTORY_DOES_NOT_EXIST);
            } else {
                returnValue = new MvReturnValue(StatusCodes.Code.FILE_OR_DIRECTORY_ALREADY_EXISTS);
            }
        }
        return returnValue;
    }

    public boolean directoryExists(Path path) {
        return fileManager.getFolder(path) != null;
    }

    public LsReturnValue listDirectory(Path path) {
        LsReturnValue returnValue;

        Folder directory = fileManager.getFolder(path);
        if (directory == null) {
            returnValue = new LsReturnValue(StatusCodes.Code.FILE_OR_DIRECTORY_DOES_NOT_EXIST, null, null);
        } else {
            List<String> folders = new LinkedList<>();
            for (Folder folder : directory.getFolders()) {
                folders.add(folder.getName());
            }
            List<String> files = new LinkedList<>();
            for (File file : directory.getFiles()) {
                files.add(file.getName());
            }
            returnValue = new LsReturnValue(StatusCodes.Code.OK, folders, files);
        }

        return returnValue;
    }

    public MkDirReturnValue makeDirectory(Path path) {
        MkDirReturnValue returnValue;

        Path parentDirectoryPath = path.getParent();
        String folderName = path.getFileName().toString();

        Folder parentDirectory = fileManager.getFolder(parentDirectoryPath);

        if (parentDirectory == null) {
            returnValue = new MkDirReturnValue(StatusCodes.Code.FILE_OR_DIRECTORY_DOES_NOT_EXIST);
        } else {
            if (parentDirectory.folderExists(folderName)) {
                returnValue = new MkDirReturnValue(StatusCodes.Code.FILE_OR_DIRECTORY_ALREADY_EXISTS);
            } else {
                Folder folder = new Folder(folderName);
                parentDirectory.addFolder(folder);

                returnValue = new MkDirReturnValue(StatusCodes.Code.OK);
            }
        }
        return returnValue;
    }

}
