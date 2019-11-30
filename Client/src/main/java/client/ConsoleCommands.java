package client;

import commons.Ports;
import commons.StatusCodes;
import commons.commands.general.FileUploadAck;
import commons.commands.naming.NamingCommand;
import commons.commands.storage.AskReady;
import commons.commands.storage.ConfirmReady;
import commons.commands.storage.StorageCommand;
import commons.routines.IORoutines;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class ConsoleCommands {

    private String downloadsDir;
    private final String defaultDir = "/";
    private final String defaultDownloadDir = "downloads";
    private String currentRemoteDir = "/";

    private String hostNaming = "10.91.51.171";
    private Scanner in = new Scanner(System.in);


    public String getHostNaming() {
        return this.hostNaming;
    }

    public void setHostNaming(String hostNaming) {
        this.hostNaming = hostNaming;
    }

    public String getDefaultDownloadDir() {
        return this.defaultDownloadDir;
    }

    public String getCurrentRemoteDir() {
        return currentRemoteDir;
    }

    public void setCurrentRemoteDir(String currentRemoteDir) {
        this.currentRemoteDir = currentRemoteDir;
    }

    public String getCurrentDownloadDir() {
        return this.downloadsDir;
    }

    public void setCurrentDownloadsDir(String newDownloadDir) {
        this.downloadsDir = newDownloadDir;
    }

    public String getAbsolutePath(String path) {
        if ((path.charAt(0) == '.' && path.charAt(1) == '/')) {
            return this.getCurrentRemoteDir() + path.substring(2);
        } else if (path.charAt(0) == '/') {
            return path;
        } else if ((path.charAt(0) != '.' && path.charAt(0) != '/')) {
            return this.getCurrentRemoteDir() + path;

        }
        return "-1";
    }

    public Scanner getInput() {
        return this.in;
    }

    public void help() {
        System.out.println("Available commands: ");
        System.out.println("0. `init` - clear all");
        System.out.println("1. `touch <new_file_path>` - create empty file");
        System.out.println("2. `get <remote path> (local path)` - download file");
        System.out.println("3. `put <local path> (remote path)` - upload file");
        System.out.println("4. `rm <path>` - delete file");
        System.out.println("5. `info <path>` - file info");
        System.out.println("6. `cp <from> <to>` - copy file");
        System.out.println("7. `mv <from> <to>` - move file");
        System.out.println("8. `cd <path>` - open directory");
        System.out.println("9. `ls (path)` - read directory");
        System.out.println("10. `mkdir (path)` - create directory");
        System.out.println("11. `help` - list available commands");
        System.out.println("12. `setdd <path>` - set a directory for downloads");
        System.out.println("13. `getdd` - get current directory for downloads\n");
        System.out.println("\n`<arg>` - required argument, `(arg)` - optional argument\n");
    }

    public void init() throws IOException, ClassNotFoundException {
        System.out.println("Clear the storage? (yes/no)\nThis action CANNOT BE UNDONE ");
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
                    }else{
                        System.out.println("Invalid input.");
                }
                }
            }
        }

    }


    private void init_yes() throws IOException, ClassNotFoundException {
        Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.Init();
        IORoutines.sendSignal(socket, namingCommand);
        commons.commands.naming.InitAck receiveAkn = (commons.commands.naming.InitAck) IORoutines.receiveSignal(socket);
        System.out.println(receiveAkn.getStatus());
    }


    public void touch(String newFilePath) throws IOException, ClassNotFoundException {
        // Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
        newFilePath = this.getAbsolutePath(newFilePath);
        System.out.println(newFilePath);
//        NamingCommand namingCommand = new commons.commands.naming.TouchFile(newFilePath);
//        IORoutines.sendSignal(socket, namingCommand);
//        commons.commands.naming.TouchAck receiveAkn = (commons.commands.naming.TouchAck) IORoutines.receiveSignal(socket);
//        System.out.println(receiveAkn.getStatus());
    }

    public void get(String[] filePaths) throws IOException, ClassNotFoundException {
        //todo NAMING_SERVER_CONNECTION
        Socket namingSocket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.Get(filePaths[0]);
        IORoutines.sendSignal(namingSocket, namingCommand);
        commons.commands.naming.GetAck receiveAknName = (commons.commands.naming.GetAck) IORoutines.receiveSignal(namingSocket);

        System.out.println(receiveAknName.getStatus());
        InetAddress hostStorage = receiveAknName.getNodeAddress();
        UUID fileId = receiveAknName.getFileId();
//        InetAddress hostStorage = InetAddress.getByName("10.91.51.200");
//        UUID fileId = UUID.fromString("ad99feb3-d4ac-4b99-8f3d-7b9353d34468");

        String localFileName;
        if (filePaths.length == 1) {
            String[] fileDirChain = filePaths[0].split("/");
//            System.out.println(this.getCurrentDownloadDir());
            localFileName = this.getCurrentDownloadDir() + fileDirChain[fileDirChain.length - 1];
        } else {
            localFileName = this.getCurrentDownloadDir() + filePaths[1];
        }

        //todo STORAGE_SERVER_CONNECTION
        Socket storageSocket = new Socket(hostStorage, Ports.PORT_STORAGE);

        StorageCommand storageCommand = new commons.commands.storage.FileDownload(fileId);
        IORoutines.sendSignal(storageSocket, storageCommand);

        AskReady storageCommand1 = (AskReady) IORoutines.receiveSignal(storageSocket);
//todo get from storageCommand the size of file to show the progess_bar
        ConfirmReady storageCommand2 = new ConfirmReady();
        IORoutines.sendSignal(storageSocket, storageCommand2);

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

    public void put(String[] filePaths) throws IOException, ClassNotFoundException {

        //todo NAMING_SERVER_CONNECTION
        String remoteFileName;
        if (filePaths.length == 1) {
            String[] fileDirChain = filePaths[0].split("/");
            remoteFileName = this.getCurrentRemoteDir() + fileDirChain[fileDirChain.length - 1];
        } else {
            remoteFileName = this.getCurrentRemoteDir() + filePaths[1];
        }

        NamingCommand namingCommand = new commons.commands.naming.PutFile(remoteFileName);

        Socket namingSocket = new Socket(hostNaming, Ports.PORT_NAMING);
        IORoutines.sendSignal(namingSocket, namingCommand);
        commons.commands.naming.PutAck receiveAknName = (commons.commands.naming.PutAck) IORoutines.receiveSignal(namingSocket);

        InetAddress hostStorage = receiveAknName.getStorageAddress();
        UUID fileId = receiveAknName.getFileId();
        Collection<InetAddress> replicasAddresses = receiveAknName.getReplicaAddresses();
        if (receiveAknName.getStatus().equals(StatusCodes.Code.OK)) {
            System.out.println(hostStorage + " " + fileId + " " + replicasAddresses.toString());
        } else {
            System.out.println(receiveAknName.getStatus());
        }
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
        AskReady storageCommand1 = (AskReady) IORoutines.receiveSignal(storageSocket);


        OutputStream uploadingToServer = storageSocket.getOutputStream();
        InputStream readingTheFile = new FileInputStream(filePaths[0]);
        IORoutines.transmit(readingTheFile, uploadingToServer);


//        receiveAknStor =
//                (commons.commands.general.FileDownloadAck) IORoutines.receiveSignal(storageSocket);
//
//        System.out.println(receiveAknStor.getStatusCode());
        FileUploadAck storageCommand2 = (FileUploadAck) IORoutines.receiveSignal(storageSocket);

        System.out.println(storageCommand2.getStatusCode());
        uploadingToServer.close();
        readingTheFile.close();
    }

    public void rm(String fileOrDirPath) throws IOException, ClassNotFoundException {
        Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.RmFile(fileOrDirPath);
        IORoutines.sendSignal(socket, namingCommand);
        commons.commands.naming.RmAck receiveAkn = (commons.commands.naming.RmAck) IORoutines.receiveSignal(socket);
        System.out.println(receiveAkn.getStatus());
    }

    public void info(String filePath) throws IOException, ClassNotFoundException {

        Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.InfoFile(filePath);
        IORoutines.sendSignal(socket, namingCommand);
        //TODO output path
        commons.commands.naming.InfoAck receiveAkn = (commons.commands.naming.InfoAck) IORoutines.receiveSignal(socket);
        System.out.println(String.valueOf(receiveAkn.getStatus()) + receiveAkn.getNodes().toString() +
                "\n" + receiveAkn.getFileSize() + "\n" + receiveAkn.getAccessRights());
    }

    public void cp(String fromPath, String toPath) throws IOException, ClassNotFoundException {
        Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.CpFile(fromPath, toPath);
        IORoutines.sendSignal(socket, namingCommand);
        commons.commands.naming.CpAck receiveAkn = (commons.commands.naming.CpAck) IORoutines.receiveSignal(socket);
        System.out.println(receiveAkn.getStatus());
    }

    public void mv(String fromPath, String toPath) throws IOException, ClassNotFoundException {
        Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.MvFile(fromPath, toPath);
        IORoutines.sendSignal(socket, namingCommand);
        commons.commands.naming.MvAck receiveAkn = (commons.commands.naming.MvAck) IORoutines.receiveSignal(socket);
        System.out.println(receiveAkn.getStatus());
    }

    public void cd(String dirPath) throws IOException, ClassNotFoundException {
        dirPath = this.getAbsolutePath(dirPath);
        System.out.println(dirPath);
        this.setCurrentRemoteDir(dirPath);

//        Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
//        NamingCommand namingCommand = new commons.commands.naming.Cd(dirPath);
//        IORoutines.sendSignal(socket, namingCommand);
//        commons.commands.naming.CdAck receiveAkn = (commons.commands.naming.CdAck) IORoutines.receiveSignal(socket);
//        StatusCodes.Code status = receiveAkn.getStatus();
//        System.out.println(status);
//        if (!(status.equals(StatusCodes.Code.FILE_OR_DIRECTORY_DOES_NOT_EXIST))) {
//            this.setCurrentRemoteDir(dirPath);
//        }
    }

    public void ls(String dirPath) throws IOException, ClassNotFoundException {

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

    public void mkdir(String dirPath) throws IOException, ClassNotFoundException {
        Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.MkDir(dirPath);
        IORoutines.sendSignal(socket, namingCommand);
        commons.commands.naming.MkdirAck receiveAkn = (commons.commands.naming.MkdirAck) IORoutines.receiveSignal(socket);
        System.out.println(receiveAkn.getStatus());
    }

    private void displayProgress(Boolean b) {
        while (b) {
            System.out.print("/\r");
            System.out.flush();
            System.out.print("-\r");
            System.out.flush();
            System.out.print("\\\r");
            System.out.flush();
            System.out.print("|\r");
            System.out.flush();
        }
    }
}
