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
import java.util.*;

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
                    } else {
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
        System.out.println(receiveAkn.getStatusStr());
    }


    public void touch(String newFilePath) throws IOException, ClassNotFoundException {
        newFilePath = this.getAbsolutePath(newFilePath);

        Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
        System.out.println("file_path: " + newFilePath);
        NamingCommand namingCommand = new commons.commands.naming.TouchFile(newFilePath);
        IORoutines.sendSignal(socket, namingCommand);
        commons.commands.naming.TouchAck receiveAkn = (commons.commands.naming.TouchAck) IORoutines.receiveSignal(socket);
        if (receiveAkn.getStatusCode() == StatusCodes.OK) {
            System.out.println(receiveAkn.getStatusStr());
        } else {
            System.out.println(receiveAkn.getStatusStr());
        }
    }

    public void get(String[] filePaths) throws IOException, ClassNotFoundException {
        //NAMING_SERVER_CONNECTION
        String localFileName;
        if (filePaths.length == 1) {
            String[] fileDirChain = filePaths[0].split("/");
//            System.out.println(this.getCurrentDownloadDir());
            localFileName = this.getCurrentDownloadDir() + "/" + fileDirChain[fileDirChain.length - 1];
        } else {
            localFileName = this.getCurrentDownloadDir() + "/" + filePaths[1];
        }

        Socket namingSocket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.Get(filePaths[0]);
        IORoutines.sendSignal(namingSocket, namingCommand);
        commons.commands.naming.GetAck receiveAknName = (commons.commands.naming.GetAck) IORoutines.receiveSignal(namingSocket);
        int statusCode = receiveAknName.getStatusCode();
        if (statusCode == StatusCodes.IS_TOUCHED) {
            //todo create empty file
            File file = new File(localFileName);
            if (file.createNewFile()) {
                System.out.println("Empty file" + localFileName + " downloaded successfully");
            } else {
                System.out.println("Error downloading: file " + localFileName + " already exists on client side");
            }
        } else if (statusCode == StatusCodes.OK) {
            InetAddress hostStorage = receiveAknName.getNodeAddress();
            UUID fileId = receiveAknName.getFileId();

//        InetAddress hostStorage = InetAddress.getByName("10.91.51.200");
//        UUID fileId = UUID.fromString("6ce6d50c-ac9e-46af-9214-fc1374702ac4");


            //STORAGE_SERVER_CONNECTION
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

            System.out.println("Downloaded");
            savingTheFile.close();
            downloading.close();
        } else {
            System.out.println(receiveAknName.getStatusStr());
        }
    }

    public void put(String[] filePaths) throws IOException, ClassNotFoundException {
// todo catch exc no file found here
        // NAMING_SERVER_CONNECTION
        String remoteFileName;
        if (filePaths.length == 1) {
            String[] fileDirChain = filePaths[0].split("/");
            remoteFileName = this.getCurrentRemoteDir() + "/" + fileDirChain[fileDirChain.length - 1];
        } else {
            remoteFileName = this.getCurrentRemoteDir() + "/" + filePaths[1];
        }

        File file = new File(this.getAbsolutePath(filePaths[0]));
        boolean[] rwe = {file.canRead(), file.canWrite(), file.canExecute()};
        StringBuilder rights = new StringBuilder();

        for (boolean b : rwe) {
            System.out.println(b);
            if (b) {
                rights.append("1");
            } else {
                rights.append("0");
            }
        }

        NamingCommand namingCommand = new commons.commands.naming.PutFile(remoteFileName, rights.toString(), file.length());

        Socket namingSocket = new Socket(hostNaming, Ports.PORT_NAMING);
        IORoutines.sendSignal(namingSocket, namingCommand);
        commons.commands.naming.PutAck receiveAknName = (commons.commands.naming.PutAck) IORoutines.receiveSignal(namingSocket);
        if (receiveAknName.getStatusCode() != (StatusCodes.OK)) {
            System.out.println(receiveAknName.getStatusStr());
        } else {
            InetAddress hostStorage = receiveAknName.getStorageAddress();
            UUID fileId = receiveAknName.getFileId();
            Collection<InetAddress> replicasAddresses = receiveAknName.getReplicaAddresses();
            if (receiveAknName.getStatusCode() == (StatusCodes.OK)) {
                System.out.println(hostStorage + " " + fileId + " " + replicasAddresses.toString());
            } else {
                System.out.println(receiveAknName.getStatusStr());
            }
//        Collection<InetAddress> replicasAddresses = new LinkedList<>();
//        UUID fileId = UUID.randomUUID();
//        InetAddress hostStorage = InetAddress.getByName("10.91.51.200");
            //todo STORAGE_SERVER_CONNECTION
            Socket storageSocket = new Socket(hostStorage, Ports.PORT_STORAGE);
            StorageCommand storageCommand = new commons.commands.storage.FileUpload(fileId, replicasAddresses);
            IORoutines.sendSignal(storageSocket, storageCommand);

            ConfirmReady storageCommand1 = (ConfirmReady) IORoutines.receiveSignal(storageSocket);
//            System.out.println("Agree? " + storageCommand1.isAgree());
            OutputStream uploadingToServer = storageSocket.getOutputStream();
            InputStream readingTheFile = new FileInputStream(filePaths[0]);
            IORoutines.transmit(readingTheFile, uploadingToServer);
            uploadingToServer.flush();

            FileUploadAck storageCommand2 = (FileUploadAck) IORoutines.receiveSignal(storageSocket);
            uploadingToServer.close();
            readingTheFile.close();

            System.out.println(storageCommand2.getStatusStr());
        }
    }

    public void rm(String fileOrDirPath) throws IOException, ClassNotFoundException {
        fileOrDirPath = this.getAbsolutePath(fileOrDirPath);
        Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.RmFile(fileOrDirPath);
        IORoutines.sendSignal(socket, namingCommand);
        commons.commands.naming.RmAck receiveAkn = (commons.commands.naming.RmAck) IORoutines.receiveSignal(socket);
        int statusCode = receiveAkn.getStatusCode();
        //todo get files inside the dir
        if (statusCode == StatusCodes.CONFIRMATION_REQUIRED) {
            System.out.println("Directory " + fileOrDirPath + " is not empty. Continue removing? (y/n)");
            String answer = this.in.nextLine();
            boolean answ;
            if (answer.strip().equals("y")) {
                answ = true;
            } else {
                answ = false;
            }
            commons.commands.naming.RmConfirm namingCommand1 = new commons.commands.naming.RmConfirm(answ);
            IORoutines.sendSignal(socket, namingCommand1);
            if (!answ) {
                System.out.println("Remove cancelled.");
            } else {
                commons.commands.naming.RmAck receiveAkn1 = (commons.commands.naming.RmAck) IORoutines.receiveSignal(socket);

                System.out.println(receiveAkn1.getStatusStr());
            }

        }
    }

    public void info(String filePath) throws IOException, ClassNotFoundException {
        filePath = this.getAbsolutePath(filePath);
        Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.InfoFile(filePath);
        IORoutines.sendSignal(socket, namingCommand);
        commons.commands.naming.InfoAck receiveAkn = (commons.commands.naming.InfoAck) IORoutines.receiveSignal(socket);
        if (receiveAkn.getStatusCode() == StatusCodes.OK) {
            System.out.println("abs_path: " + this.getAbsolutePath(filePath));
            System.out.println("file_size: " + receiveAkn.getFileSize());
            System.out.println("access_rights_rwm: " + receiveAkn.getAccessRights());
            System.out.println("number_of_file_replicas: " + receiveAkn.getNodes().size());
        } else {
            System.out.println(receiveAkn.getStatusStr());
        }
    }

    public void cp(String fromPath, String toPath) throws IOException, ClassNotFoundException {
        fromPath = this.getAbsolutePath(fromPath);
        toPath = this.getAbsolutePath(toPath);
        Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.CpFile(fromPath, toPath);
        IORoutines.sendSignal(socket, namingCommand);
        commons.commands.naming.CpAck receiveAkn = (commons.commands.naming.CpAck) IORoutines.receiveSignal(socket);
        System.out.println(receiveAkn.getStatusStr());
    }

    public void mv(String fromPath, String toPath) throws IOException, ClassNotFoundException {
        fromPath = this.getAbsolutePath(fromPath);
        toPath = this.getAbsolutePath(toPath);
        Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.MvFile(fromPath, toPath);
        IORoutines.sendSignal(socket, namingCommand);
        commons.commands.naming.MvAck receiveAkn = (commons.commands.naming.MvAck) IORoutines.receiveSignal(socket);
        System.out.println(receiveAkn.getStatusStr());
    }

    public void cd(String dirPath) throws IOException, ClassNotFoundException {
        dirPath = this.getAbsolutePath(dirPath);

        Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.Cd(dirPath);
        IORoutines.sendSignal(socket, namingCommand);
        commons.commands.naming.CdAck receiveAkn = (commons.commands.naming.CdAck) IORoutines.receiveSignal(socket);
        int status = receiveAkn.getStatusCode();
        System.out.println(status);
        if (status == StatusCodes.OK) {
            this.setCurrentRemoteDir(dirPath);
        }
    }

    public void ls(String dirPath) throws IOException, ClassNotFoundException {
        dirPath = this.getAbsolutePath(dirPath);
        Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.Ls(dirPath);
        IORoutines.sendSignal(socket, namingCommand);
        commons.commands.naming.LsAck receiveAkn = (commons.commands.naming.LsAck) IORoutines.receiveSignal(socket);
        int status = receiveAkn.getStatusCode();
        if (status == StatusCodes.OK) {

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

        } else {
            System.out.println(receiveAkn.getStatusStr());
        }
    }

    public void mkdir(String dirPath) throws IOException, ClassNotFoundException {
        dirPath = this.getAbsolutePath(dirPath);
        Socket socket = new Socket(hostNaming, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.MkDir(dirPath);
        IORoutines.sendSignal(socket, namingCommand);
        commons.commands.naming.MkdirAck receiveAkn = (commons.commands.naming.MkdirAck) IORoutines.receiveSignal(socket);
        if (receiveAkn.getStatusCode() == StatusCodes.OK) {
            System.out.println("Directory " + dirPath + " created");
        } else {
            System.out.println(receiveAkn.getStatusStr());
        }
        ;
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
