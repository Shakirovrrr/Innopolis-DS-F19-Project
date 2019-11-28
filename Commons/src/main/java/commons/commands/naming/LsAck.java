package commons.commands.naming;

import naming.File;
import naming.Folder;

import java.util.List;

public class LsAck extends NamingCommandAck {
    private List<Folder> folders;
    private List<File> files;

    public LsAck(int statusCode, List<Folder> folders, List<File> files) {
        this.status = statusCode;
        this.folders = folders;
        this.files = files;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public List<File> getFiles() {
        return files;
    }
}
