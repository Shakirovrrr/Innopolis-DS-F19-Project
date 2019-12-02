package client;

import commons.StatusCodes;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class ClientAPI {
    private ConsoleCommands consoleCommands;


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
            put("help", 11);//show available commands
            put("setdd", 12);// set a directory for downloads
            put("getdd", 13); // get current directory for downloads
            put("setnamip", 14);// set new naming server ip
        }
    };


    public ClientAPI(String hostNaming, String downloadsDir) {
        this.consoleCommands = new ConsoleCommands();
        this.consoleCommands.setCurrentDownloadsDir(downloadsDir);
        this.consoleCommands.setCurrentRemoteDir("/");
        this.consoleCommands.setHostNaming(hostNaming);
    }

    public ClientAPI(String hostNaming) {
        this.consoleCommands = new ConsoleCommands();
        this.consoleCommands.setCurrentDownloadsDir(this.consoleCommands.getDefaultDownloadDir());
        this.consoleCommands.setCurrentRemoteDir("/");
        this.consoleCommands.setHostNaming(hostNaming);
    }


    protected class ConsoleToken {
        int command_key;
        String[] file_dir_paths;

        ConsoleToken(int command_key, String[] file_dir_paths) {
            this.command_key = command_key;
            this.file_dir_paths = file_dir_paths;
        }
    }

    private ConsoleToken parseCommand(String input) {
        String[] input_mod = input.strip().replaceAll("\\s+", " ").split(" ");
        ;
        int key = commandsSet.getOrDefault(input_mod[0], -1);
        String[] paths = Arrays.copyOfRange(input_mod, 1, input_mod.length);
        return new ConsoleToken(key, paths);
    }

    private void commandRouter(String input) throws IOException, ClassNotFoundException {
        ConsoleToken consoleToken = parseCommand(input);
        String[] paths = consoleToken.file_dir_paths;
        switch (consoleToken.command_key) {
            case (0):
                if (paths.length == 0) {
                    this.consoleCommands.init();
                } else {
                    System.out.println("Invalid number of arguments: ``` init ``` ");
                    break;
                }
                break;
            case (1):
                if (paths.length == 1) {
                    this.consoleCommands.touch(consoleToken.file_dir_paths[0]);
                } else {
                    System.out.println("Invalid number of arguments: ``` touch <file_path> ``` ");
                    break;
                }
                break;
            case (2):
                if (paths.length == 1 || paths.length == 2) {
                    this.consoleCommands.get(consoleToken.file_dir_paths);
                } else {
                    System.out.println("Invalid number of arguments: ``` get <remote_path> <local_path> ```  or ```get <remote_path>``` ");
                    break;
                }
                break;
            case (3):
                if (paths.length == 1 || paths.length == 2) {
                    this.consoleCommands.put(consoleToken.file_dir_paths);
                } else {
                    System.out.println("Invalid number of arguments: ``` put <local_path> <remote_path> ```  or ```get <local_path>``` ");
                    break;
                }
                break;
            case (4):
                if (paths.length == 1) {
                    this.consoleCommands.rm(paths[0], true);
                } else {
                    System.out.println("Invalid number of arguments: ``` rm <file_path> ``` ");
                    break;
                }
                break;
            case (5):
                if (paths.length == 1) {
                    this.consoleCommands.info(paths[0]);
                } else {
                    System.out.println("Invalid number of arguments: ``` info <file_path> ``` ");
                    break;
                }
                break;
            case (6):
                if (paths.length == 2) {
                    this.consoleCommands.cp(paths[0], paths[1]);
                } else {
                    System.out.println("Invalid number of arguments: ``` cp <from> <to> ``` ");
                    break;
                }
                break;
            case (7):
                if (paths.length == 2) {
                    this.consoleCommands.mv(paths[0], paths[1]);
                } else {
                    System.out.println("Invalid number of arguments: ``` mv <from> <to> ``` ");
                    break;
                }
                break;
            case (8):
                if (paths.length == 1) {
                    this.consoleCommands.cd(paths[0]);
                } else {
                    System.out.println("Invalid number of arguments: ``` cd <file_path> ``` ");
                    break;
                }
                break;
            case (9):
                if (paths.length == 1) {
                    this.consoleCommands.ls(paths[0]);
                } else if (paths.length == 0) {
                    this.consoleCommands.ls(this.consoleCommands.getCurrentRemoteDir());
                } else {
                    System.out.println("Invalid number of arguments: ``` ls <file_path> ``` or ``` ls ``` ");
                    break;
                }
                break;
            case (10):
                if (paths.length == 1) {
                    this.consoleCommands.mkdir(paths[0]);
                } else {
                    System.out.println("Invalid number of arguments: ``` mkdir <path> ``` ");
                    break;
                }
                break;
            case (11):
                if (paths.length == 0) {
                    this.consoleCommands.help();
                } else {
                    System.out.println("Invalid number of arguments: ``` help ``` ");
                    break;
                }
                break;
            case (12):
                if (paths.length == 1) {
                    this.consoleCommands.setCurrentDownloadsDir(paths[0]);
                } else {
                    System.out.println("Invalid number of arguments: ``` set-download-dir <path> ``` ");
                    break;
                }
                break;
            case (13):
                if (paths.length == 0) {
                    System.out.println(this.consoleCommands.getCurrentDownloadDir());
                } else {
                    System.out.println("Invalid number of arguments: ``` get-download-dir <path> ``` ");
                    break;
                }
                break;
            case (14):
                if (paths.length == 1) {
                    this.consoleCommands.setHostNaming(paths[0]);
                    System.out.println(this.consoleCommands.getStatusStr(StatusCodes.OK));
                } else {
                    System.out.println("Invalid number of arguments: ``` setnameip <ip> ``` ");
                    break;
                }
                break;
            default:
                System.out.println("Incorrect input");
                break;

        }
    }

    public void commandHandler() {

        //connect to console
        //input - next line in console after enter press
        System.out.println("\nWelcome to the Distributed Storage!\nEnter 'help' for listing the commands");
        File dir0 = new File("." + this.consoleCommands.getCurrentUploadDir());
        System.out.println();
        if (!dir0.exists()) {
            if (dir0.mkdir()) {
                System.out.println("Upload directory" + dir0.getAbsolutePath() + "created");
            } else {
                System.out.println("Fail to create upload directory " + dir0.getAbsolutePath());
            }
        } else {
            System.out.println("Upload directory " + dir0.getAbsolutePath() + " already exists");
        }
        System.out.println("For uploading a file should be in project directory (default - " + this.consoleCommands.getCurrentUploadDir() + ")");
        File dir1 = new File("." + this.consoleCommands.getCurrentDownloadDir());
        System.out.println();
        if (!dir1.exists()) {
            if (dir1.mkdir()) {
                System.out.println("Download directory" + dir1.getAbsolutePath() + "created");
            } else {
                System.out.println("Fail to create download directory " + dir1.getAbsolutePath());
            }
        } else {
            System.out.println("Download directory " + dir1.getAbsolutePath() + " already exists");
        }
        System.out.println();
        System.out.print("\nstorage:" + this.consoleCommands.getCurrentRemoteDir() + " " + "$ ");
        while (true) {
            if (this.consoleCommands.getInput().hasNextLine()) {
                String input = this.consoleCommands.getInput().nextLine();
                try {

                    commandRouter(input);

                    System.out.print("\nstorage:" + this.consoleCommands.getCurrentRemoteDir() + " " + "$ ");
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }
}