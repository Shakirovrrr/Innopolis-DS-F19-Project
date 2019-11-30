package client;

import commons.Ports;
import commons.StatusCodes;
import commons.commands.Command;
import commons.commands.naming.NamingCommand;
import commons.commands.storage.StorageCommand;
import commons.routines.IORoutines;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;

public class ClientAPI {
    String downloadsDir;

    private String hostNaming;
    private static final String defaultDir = "root";
    private static final String defaultDownloadDir = "./downloads";

    private Scanner in = new Scanner(System.in);
    private String currentRemoteDir;
    private HashMap<String, Integer> commandsSet = new HashMap<>() {
        {
            put("init", 0); //clear all
            put("touch", 1); // create empty file
            put("get", 2); //download file
            put("put", 3);//upload file
            put("rm", 4);//delete file
            put("info", 5);//file info
            put("cp", 6);//copy
            put("mv", 7);//move file
            put("cd", 8);//open dir
            put("ls", 9);//read dir
            put("mkdir", 10);//create dir

        }
    };

    public ClientAPI(String downloadsDir) {
        this.downloadsDir = downloadsDir;
        this.currentRemoteDir = defaultDir;
    }

    public ClientAPI() {
        this.downloadsDir = defaultDownloadDir;
        this.currentRemoteDir = defaultDir;
    }

    public String getCurrentRemoteDir() {
        return currentRemoteDir;
    }

    public void setCurrentRemoteDir(String currentRemoteDir) {
        this.currentRemoteDir = currentRemoteDir;
    }

    public String getCurrentDownloadDir() {
        return downloadsDir;
    }

    public void setCurrentDownloadsDir(String newDownloadDir) {
        this.downloadsDir = newDownloadDir;
    }

    //TODO add a command that allows changing download dir inside the console
    protected class ConsoleToken {
        int command_key;
        String[] file_dir_paths;

        ConsoleToken(int command_key, String[] file_dir_paths) {
            this.command_key = command_key;
            this.file_dir_paths = file_dir_paths;
        }
    }

    private ConsoleToken parseCommand(String input) {
        String[] input_mod = input.split(" ");
        int key = commandsSet.getOrDefault(input_mod[0], -1);
        String[] paths = Arrays.copyOfRange(input_mod, 1, input_mod.length);
        return new ConsoleToken(key, paths);
    }

    protected void commandRouter(String input) throws IOException, ClassNotFoundException {
        ConsoleToken consoleToken = parseCommand(input);
        String[] paths = consoleToken.file_dir_paths;
        switch (consoleToken.command_key) {
            case (0):
                if (paths.length > 0) {
                    init();
                } else {
                    System.out.println("Invalid number of arguments: ``` init ``` ");
                    break;
                }
                break;
            case (1):
                if (paths.length == 1) {
                    touch(consoleToken.file_dir_paths[0]);
                } else {
                    System.out.println("Invalid number of arguments: ``` touch <file_path> ``` ");
                    break;
                }
                break;
            case (2):
                if (paths.length == 1 || paths.length == 2) {
                    get(consoleToken.file_dir_paths);
                } else {
                    System.out.println("Invalid number of arguments: ``` get <remote_path> <local_path> ```  or ```get <remote_path>``` ");
                    break;
                }
                break;
            case (3):
                if (paths.length == 1 || paths.length == 2) {
                    put(consoleToken.file_dir_paths);
                } else {
                    System.out.println("Invalid number of arguments: ``` put <local_path> <remote_path> ```  or ```get <local_path>``` ");
                    break;
                }
                break;
            case (4):
                if (paths.length == 1) {
                    rm(paths[0]);
                } else {
                    System.out.println("Invalid number of arguments: ``` rm <file_path> ``` ");
                    break;
                }
                break;
            case (5):
                if (paths.length == 1) {
                    info(paths[0]);
                } else {
                    System.out.println("Invalid number of arguments: ``` info <file_path> ``` ");
                    break;
                }
                break;
            case (6):
                if (paths.length == 2) {
                    cp(paths[0], paths[1]);
                } else {
                    System.out.println("Invalid number of arguments: ``` cp <from> <to> ``` ");
                    break;
                }
                break;
            case (7):
                if (paths.length == 2) {
                    mv(paths[0], paths[1]);
                } else {
                    System.out.println("Invalid number of arguments: ``` mv <from> <to> ``` ");
                    break;
                }
                break;
            case (8):
                if (paths.length == 1) {
                    cd(paths[0]);
                } else {
                    System.out.println("Invalid number of arguments: ``` cd <file_path> ``` ");
                    break;
                }
                break;
            case (9):
                if (paths.length == 1) {
                    ls(paths[0]);
                } else if (paths.length == 0) {
                    ls(this.getCurrentRemoteDir());
                } else {
                    System.out.println("Invalid number of arguments: ``` ls <file_path> ``` or ``` ls ``` ");
                    break;
                }
                break;
            case (10):
                if (paths.length == 1) {
                    mkdir(paths[0]);
                } else {
                    System.out.println("Invalid number of arguments: ``` mkdir <path> ``` ");
                    break;
                }
                break;

        }
    }

    private void init_yes() throws IOException, ClassNotFoundException {
        Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.Init();
        IORoutines.sendSignal(socket, namingCommand);
        commons.commands.naming.InitAck receiveAkn = (commons.commands.naming.InitAck) IORoutines.receiveSignal(socket);
        System.out.println(receiveAkn.getStatus());
    }

    public void init() throws IOException, ClassNotFoundException {
        System.out.println("Clear the storage? (yes/no)\n This action CANNOT BE UNDONE ");
        if (this.in.hasNextLine()) {
            String answer = this.in.nextLine();
            if (answer.equals("yes")) {
                init_yes();
            } else if (answer.equals("no")) {
                System.out.println("Aborted.");
            } else {
                System.out.println("Type either (yes/no), please ");

                if (this.in.hasNextLine()) {
                    answer = this.in.nextLine();
                    if (answer.equals("yes")) {
                        init_yes();
                    }
                    if (answer.equals("no")) {
                        System.out.println("Aborted.");
                    }
                }
            }
        }

    }

    private void touch(String newFilePath) throws IOException, ClassNotFoundException {
        Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.TouchFile(newFilePath);
        IORoutines.sendSignal(socket, namingCommand);
        commons.commands.naming.TouchAck receiveAkn = (commons.commands.naming.TouchAck) IORoutines.receiveSignal(socket);
        System.out.println(receiveAkn.getStatus());
    }

    private void get(String[] filePaths) throws IOException, ClassNotFoundException {
//
//        //todo NAMING_SERVER_CONNECTION
        Socket namingSocket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.Get(filePaths[0]);
        IORoutines.sendSignal(namingSocket, namingCommand);
        commons.commands.naming.GetAck receiveAknName = (commons.commands.naming.GetAck) IORoutines.receiveSignal(namingSocket);

        InetAddress hostStorage = receiveAknName.getNodeAddress();
        UUID fileId = receiveAknName.getFileId();
//        InetAddress hostStorage = InetAddress.getByName("10.91.51.200");
//        UUID fileId = UUID.fromString("ad99feb3-d4ac-4b99-8f3d-7b9353d34468");

        String localFileName;
        if (filePaths.length == 1) {
            String[] fileDirChain = filePaths[0].split("/");
//            System.out.println(this.getCurrentDownloadDir());
            localFileName = this.getCurrentDownloadDir() + "/" + fileDirChain[fileDirChain.length - 1];
        } else {
            localFileName = this.getCurrentDownloadDir() + "/" + filePaths[1];
        }

        //todo STORAGE_SERVER_CONNECTION
        Socket storageSocket = new Socket(hostStorage, Ports.PORT_STORAGE);
        StorageCommand storageCommand = new commons.commands.storage.FileDownload(fileId);
        IORoutines.sendSignal(storageSocket, storageCommand);

        InputStream downloading = storageSocket.getInputStream();
        OutputStream savingTheFile = new FileOutputStream(localFileName);
        IORoutines.transmit(downloading, savingTheFile);
        System.out.println(localFileName);
//        commons.commands.general.FileDownloadAck receiveAknStor =
//                (commons.commands.general.FileDownloadAck) IORoutines.receiveSignal(storageSocket);
//
//        System.out.println(receiveAknStor.getStatusCode());
        System.out.println("Downloaded");
        savingTheFile.close();
        downloading.close();
    }


    private void put(String[] filePaths) throws IOException, ClassNotFoundException {

        //todo NAMING_SERVER_CONNECTION
        String remoteFileName;
        if (filePaths.length == 1) {
            String[] fileDirChain = filePaths[0].split("/");
            remoteFileName = this.getCurrentRemoteDir() + "/" + fileDirChain[fileDirChain.length - 1];
        } else {
            remoteFileName = this.getCurrentRemoteDir() + "/" + filePaths[1];
        }

        NamingCommand namingCommand = new commons.commands.naming.PutFile(remoteFileName);

        Socket namingSocket = new Socket(hostNaming, Ports.PORT_NAMING);
        IORoutines.sendSignal(namingSocket, namingCommand);
        commons.commands.naming.PutAck receiveAknName = (commons.commands.naming.PutAck) IORoutines.receiveSignal(namingSocket);

        InetAddress hostStorage = receiveAknName.getStorageAddress();
        UUID fileId = receiveAknName.getFileId();
         Collection<InetAddress> replicasAddresses = receiveAknName.getReplicaAddresses();
//
//        Collection<InetAddress> replicasAddresses = new LinkedList<>();
//        UUID fileId = UUID.randomUUID();
//        InetAddress hostStorage = InetAddress.getByName("10.91.51.200");
        //todo STORAGE_SERVER_CONNECTION
        Socket storageSocket = new Socket(hostStorage, Ports.PORT_STORAGE);
        StorageCommand storageCommand = new commons.commands.storage.FileUpload(fileId, replicasAddresses);
        IORoutines.sendSignal(storageSocket, storageCommand);
//        commons.commands.general.FileDownloadAck receiveAknStor =
//                (commons.commands.general.FileDownloadAck) IORoutines.receiveSignal(storageSocket);
//        if(! (receiveAknStor.getStatusCode()).equals(StatusCodes.Code.OK)){
//            System.out.println("Error connection with storage");
//        }
//        else{


        OutputStream uploadingToServer = storageSocket.getOutputStream();
        InputStream readingTheFile = new FileInputStream(filePaths[0]);
        IORoutines.transmit(readingTheFile, uploadingToServer);


//        receiveAknStor =
//                (commons.commands.general.FileDownloadAck) IORoutines.receiveSignal(storageSocket);
//
//        System.out.println(receiveAknStor.getStatusCode());
        System.out.println("Uploaded");
        uploadingToServer.close();
        readingTheFile.close();
    }


    private void rm(String fileOrDirPath) throws IOException, ClassNotFoundException {
        Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.RmFile(fileOrDirPath);
        IORoutines.sendSignal(socket, namingCommand);
        commons.commands.naming.RmAck receiveAkn = (commons.commands.naming.RmAck) IORoutines.receiveSignal(socket);
        System.out.println(receiveAkn.getStatus());
    }

    private void info(String filePath) throws IOException, ClassNotFoundException {

        Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.InfoFile(filePath);
        IORoutines.sendSignal(socket, namingCommand);
        //TODO output path
        commons.commands.naming.InfoAck receiveAkn = (commons.commands.naming.InfoAck) IORoutines.receiveSignal(socket);
        System.out.println(String.valueOf(receiveAkn.getStatus()) + receiveAkn.getNodes().toString() +
                "\n" + receiveAkn.getFileSize() + "\n" + receiveAkn.getAccessRights());
    }

    private void cp(String fromPath, String toPath) throws IOException, ClassNotFoundException {
        Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.CpFile(fromPath, toPath);
        IORoutines.sendSignal(socket, namingCommand);
        commons.commands.naming.CpAck receiveAkn = (commons.commands.naming.CpAck) IORoutines.receiveSignal(socket);
        System.out.println(receiveAkn.getStatus());
    }

    private void mv(String fromPath, String toPath) throws IOException, ClassNotFoundException {
        Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.MvFile(fromPath, toPath);
        IORoutines.sendSignal(socket, namingCommand);
        commons.commands.naming.MvAck receiveAkn = (commons.commands.naming.MvAck) IORoutines.receiveSignal(socket);
        System.out.println(receiveAkn.getStatus());
    }

    private void cd(String dirPath) throws IOException, ClassNotFoundException {
        Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.Cd(dirPath);
        IORoutines.sendSignal(socket, namingCommand);
        commons.commands.naming.CdAck receiveAkn = (commons.commands.naming.CdAck) IORoutines.receiveSignal(socket);
        StatusCodes.Code status = receiveAkn.getStatus();
        System.out.println(status);
        if (!(status.equals(StatusCodes.Code.FILE_OR_DIRECTORY_DOES_NOT_EXIST))) {
            this.setCurrentRemoteDir(dirPath);
        }
    }

    private void ls(String dirPath) throws IOException, ClassNotFoundException {

        Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.Ls(dirPath);
        IORoutines.sendSignal(socket, namingCommand);
        commons.commands.naming.LsAck receiveAkn = (commons.commands.naming.LsAck) IORoutines.receiveSignal(socket);
        StatusCodes.Code status = receiveAkn.getStatus();
        System.out.println(status);
        if (status.equals(StatusCodes.Code.OK)) {

            List<String> folders = receiveAkn.getFolders();
            List<String> files = receiveAkn.getFiles();
            if (folders != null) {
                if (!folders.isEmpty()) {
                    System.out.println("Folders: ");
                    for (String folder : folders) {
                        System.out.println(folder);
                    }
                }
            }
            if (files != null) {
                if (!files.isEmpty()) {
                    System.out.println("Files: ");
                    for (String file : files) {
                        System.out.println(file);
                    }
                }
            }

        }
    }

    private void mkdir(String dirPath) throws IOException, ClassNotFoundException {
        Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.MkDir(dirPath);
        IORoutines.sendSignal(socket, namingCommand);
        commons.commands.naming.MkdirAck receiveAkn = (commons.commands.naming.MkdirAck) IORoutines.receiveSignal(socket);
        System.out.println(receiveAkn.getStatus());
    }

    public void commandHandler() throws IOException, ClassNotFoundException {

        //connect to console
        //input - next line in console after enter press
        while (true) {
            System.out.print(this.getCurrentRemoteDir() + "/ " + "$ ");

            if (in.hasNextLine()) {
                String input = in.nextLine();

                commandRouter(input);

            }
        }
    }

}
