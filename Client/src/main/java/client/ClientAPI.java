package client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class ClientAPI {
    Scanner in = new Scanner(System.in);

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

    protected int commandRouter(String input) {
        ConsoleToken consoleToken = parseCommand(input);
        String[] paths = consoleToken.file_dir_paths;
        switch (consoleToken.command_key) {
            case (0):
                return init();
//                break;
            case (1):
                if (paths.length == 1) {
                   return touch(consoleToken.file_dir_paths[0]);
                } else {
                    return newLine();
                }
                //break;
            case (2):
                if (paths.length == 1 || paths.length == 2) {
                    return get(consoleToken.file_dir_paths);
                } else {
                    return newLine();
                }
                //break;
            case (3):
                if (paths.length == 1 || paths.length == 2) {
                    return put(paths);
                } else {
                    return newLine();
                }
               // break;
            case (4):
                if (paths.length == 1) {
                    return rm(paths[0]);
                } else {
                    return newLine();
                }
               // break;
            case (5):
                if (paths.length == 1) {
                    return info(paths[0]);
                } else {
                    return newLine();
                }
               // break;
            case (6):
                if (paths.length == 2) {
                   return cp(paths);
                } else {
                 return    newLine();
                }
                //break;
            case (7):
                if (paths.length == 2) {
                    mv(paths);
                } else {
                    return newLine();
                }
              //  break;
            case (8):
                if (paths.length == 1) {
                    cd(paths[0]);
                } else {
                    return newLine();
                }
              //  break;
            case (9):
                if (paths.length == 1) {
                    return ls(paths[0]);
                } else {
                    return newLine();
                }
             //   break;
            case (10):
                if (paths.length == 1) {
                    return mkdir(paths[0]);
                } else {
                    return newLine();
                }
              //  break;
            default:
                return newLine();
               // break;
        }
       // return 150;
    }

    public int init() {
        System.out.println("Clear the storage? No files could be restored (yes/no)");
        if (this.in.hasNextLine()) {
            String answer = this.in.nextLine();
            if (answer.equals("yes")) {
                //todo вызвать серверное удаление всего
            }
            if (answer.equals("no")) {
                System.out.println("Aborted.");
            } else {
                System.out.println("Type either (yes/no), please ");

                if (this.in.hasNextLine()) {
                    answer = this.in.nextLine();
                    if (answer.equals("yes")) {
                        //todo вызвать серверное удаление всего
                    }
                    if (answer.equals("no")) {
                        System.out.println("Aborted.");
                    } else {
                        newLine();
                    }
                }
            }
        }
        return 0;
    }
//TODO IMPORTANT:  большинство вызовов подразумевает обращнение к нейминг серверу, а потом к сторедж
    public int touch(String newFilePath) {
        //todo вызвать создание пустого файла

        return 1;
    }

    public int get(String[] filePaths) {
        //todo захендлить случай, когда файла такого нет

        if (filePaths.length == 1) {
            //todo вызвать скачивание в дефолтную директорию
            // remote_path = filePaths[0]; get(remote_path));

        }
        else {
            //todo вызвать скачивание в указанную директорию
            //remote_path = filePaths[0]; local_path = filePaths[1]; get(remote_path,local_path));

        }
        return 2;

    }

    public int put(String[] filePaths) {
        if (filePaths.length == 1) {
            //todo вызвать создание файла в руте клиента
            // local_path = filePaths[0]; get(local_path));

        }
        else {
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

    public void commandHandler() {

        //connect to console
        //input - next line in console after enter press
        while (true) {                System.out.print("$ ");

            if (in.hasNextLine()) {
                String input = in.nextLine();
                System.out.println(commandRouter(input));
            }
        }
    }

}
