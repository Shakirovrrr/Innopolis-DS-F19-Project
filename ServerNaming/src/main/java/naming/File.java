package naming;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.UUID;

public class File {
    private String name;
    private boolean isFolder;
    private long size;
    private int access;
    private UUID uuid;
    private LinkedList<InetAddress> nodes;

    public File(String name, boolean isFolder, long size, int access, UUID uuid, LinkedList<InetAddress> nodes) {
        assert !isFolder;    // make sure that the structure preserves

        this.name = name;
        this.isFolder = false;
        this.size = size;
        this.access = access;
        this.uuid = uuid;
        this.nodes = nodes;
    }

    public String getName() {
        return name;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public long getSize() {
        return size;
    }

    public int getAccess() {
        return access;
    }

    public UUID getUuid() {
        return uuid;
    }

    public LinkedList<InetAddress> getNodes() {
        return nodes;
    }
}
