package naming;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Dumper {
    private static final Path FILE_MANAGER_PATH = Paths.get("./fileManager.json");

    public static void dumpTree(FileManager fileManager) {
        Gson gson = new Gson();
        String json = gson.toJson(fileManager);

        try {
            if (!Files.exists(FILE_MANAGER_PATH)) {
                Files.createFile(FILE_MANAGER_PATH);
            }
            Files.writeString(FILE_MANAGER_PATH, json);
        } catch (IOException e) {
            System.out.println("Dumping file manager failed");
            e.printStackTrace();
        }
    }

    public static FileManager getTree() {
        try {
            if (Files.exists(FILE_MANAGER_PATH)) {
                System.out.println("Exists");
                String json = Files.readString(FILE_MANAGER_PATH);
                Gson gson = new Gson();
                return gson.fromJson(json, FileManager.class);
            }
            System.out.println("Dump file does not exist");
            return null;
        } catch (IOException e) {
            System.out.println("Error while reading dumped file");
            e.printStackTrace();
            return null;
        }
//        String json = new Gson()
    }
}
