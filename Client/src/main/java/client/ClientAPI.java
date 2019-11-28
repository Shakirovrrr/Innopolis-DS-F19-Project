package client;

import commons.Ports;
import commons.commands.Command;
import commons.commands.naming.NamingCommand;
import commons.routines.IORoutines;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class ClientAPI {

    String defaultDir = "root/";
    String host = "";
    Scanner in = new Scanner(System.in);
    private String currentRemoteDir;
    HashMap<String, Integer> commandsSet = new HashMap<>() {
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

//    public ClientAPI(Scanner in) {
//        this.in = in;
//        this.currentRemoteDir = "root";
//    }

    public String getCurrentRemoteDir() {
        return currentRemoteDir;
    }

    public void setCurrentRemoteDir(String currentRemoteDir) {
        this.currentRemoteDir = currentRemoteDir;
    }


    protected class ConsoleToken {
        int command_key;
        String[] file_dir_paths;

        ConsoleToken(int command_key, String[] file_dir_paths) {
            this.command_key = command_key;
            this.file_dir_paths = file_dir_paths;
        }
    }

    protected ConsoleToken parseCommand(String input) {
        String[] input_mod = input.split(" ");
        int key = commandsSet.getOrDefault(input_mod[0], -1);
        String[] paths = Arrays.copyOfRange(input_mod, 1, input_mod.length);
        return new ConsoleToken(key, paths);
    }

    protected int commandRouter(String input) throws IOException, ClassNotFoundException {
        ConsoleToken consoleToken = parseCommand(input);
        String[] paths = consoleToken.file_dir_paths;
        switch (consoleToken.command_key) {
            case (0):
                return init();
            case (1):
                if (paths.length == 1) {
                    return touch(consoleToken.file_dir_paths[0]);
                }
            case (2):
                if (paths.length == 1 || paths.length == 2) {
                    return get(consoleToken.file_dir_paths);
                }
            case (3):
                if (paths.length == 1 || paths.length == 2) {
                    return put(paths);
                }
            case (4):
                if (paths.length == 1) {
                    return rm(paths[0]);
                }
            case (5):
                if (paths.length == 1) {
                    return info(paths[0]);
                }
            case (6):
                if (paths.length == 2) {
                    return cp(paths);
                }
            case (7):
                if (paths.length == 2) {
                    mv(paths);
                }
            case (8):
                if (paths.length == 1) {
                    cd(paths[0]);
                }
            case (9):
                if (paths.length == 1) {
                    return ls(paths[0]);
                }
            case (10):
                if (paths.length == 1) {
                    return mkdir(paths[0]);
                }

        }
        return 0;
    }

    private int init_yes() throws IOException, ClassNotFoundException {
        Socket socket = new Socket(host, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.Init();
        // WILL THERE BE A RESPONSE FROM NAMING??
        IORoutines.sendSignal(socket, namingCommand);
        Command receiveAkn = IORoutines.receiveSignal(socket);

        return 0;
    }

    public int init() throws IOException, ClassNotFoundException {
        System.out.println("Clear the storage? (yes/no)\n This action CANNOT BE UNDONE ");
        if (this.in.hasNextLine()) {
            String answer = this.in.nextLine();
            if (answer.equals("yes")) {
                init_yes();
            }
            if (answer.equals("no")) {
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
        return 0;
    }

    public int touch(String newFilePath) throws IOException, ClassNotFoundException {
        Socket socket = new Socket(host, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.TouchFile(newFilePath);
        IORoutines.sendSignal(socket, namingCommand);
        Command receiveAkn = IORoutines.receiveSignal(socket);
        return 0;
    }

    public int get(String[] filePaths) throws IOException, ClassNotFoundException {
        //NAMING_SERVER_SHOUKD захендлить случай, когда файла такого нет

        //todo NAMING_SERVER_CONNECTION
        Socket namingSocket = new Socket(host, Ports.PORT_NAMING);
        NamingCommand namingCommand = new commons.commands.naming.Get(filePaths[0]);
        IORoutines.sendSignal(namingSocket, namingCommand);
        Command receiveAknName = IORoutines.receiveSignal(namingSocket);
        //todo STORAGE_SERVER_CONNECTION
        Socket storageSocket = new Socket(host, Ports.PORT_STORAGE);
        String localFileName = "";
        if (filePaths.length == 1) {
            localFileName = filePaths[0];
        } else {
            localFileName = filePaths[1];
        }
        OutputStream fileOut = storageSocket.getOutputStream();
        InputStream downloadedFile = new FileInputStream(localFileName);
        IORoutines.transmit(downloadedFile, fileOut);
        Command receiveAknStor = IORoutines.receiveSignal(storageSocket);
        downloadedFile.close();
        fileOut.close();
        return 0;
    }



    public int put(String[] filePaths) {
        if (filePaths.length == 1) {
            //todo вызвать создание файла в руте клиента
            // local_path = filePaths[0]; get(local_path));

        } else {
            //todo вызвать создание файла в указанной директории клиента
            //local_path = filePaths[0]; remote_path = filePaths[1]; get(local_path,remote_path)); }

        }
        return 3;
    }

    public int rm(String fileOrDirPath) {
        // todo захендлить случай, когда файла такого нет
        //todo вызвать удаление файла по пути
        return 4;
    }

    public int info(String fileorPath) {
        // todo захендлить случай, когда файла такого нет
        // todo вызвать получение инфы по файлу
        return 5;
    }

    public int cp(String[] filePaths) {
        //todo захендлить случай, когда нет from_dir
        // from_dir=filePaths[0]; to_dir=filePaths[1];
        return 6;
    }

    public int mv(String[] filePaths) {
        //todo захендлить случай, когда нет from_dir
        // from_dir=filePaths[0]; to_dir=filePaths[1];
        return 7;
    }

    public int cd(String dirPath) {
        //todo захендлить случай, когда нет указанной директории

        return 8;
    }

    public int ls(String dirPath) {
        // todo захендлить случай, когда указанной директории нет такого нет
        // todo вызвать получение инфы по директории(может быть передана пустая строка)
        return 9;
    }

    public int mkdir(String dirPath) {
        // todo вызвать создание новой директории
        return 10;
    }

    private int newLine() {
        return 100;
    }

    public void commandHandler() throws IOException, ClassNotFoundException {

        //connect to console
        //input - next line in console after enter press
        while (true) {
            System.out.print("$ ");

            if (in.hasNextLine()) {
                String input = in.nextLine();
                System.out.println();
                //
//                System.out.println(commandRouter(input)input);

            }
        }
    }

}
