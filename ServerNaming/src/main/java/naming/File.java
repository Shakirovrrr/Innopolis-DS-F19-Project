package naming;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class File {
    private String name;
    private long size;
    private int access;
    private UUID uuid;
    private Set<Node> nodes;

    public File(String name, long size, int access, UUID uuid) {
        this.name = name;
        this.size = size;
        this.access = access;
        this.uuid = uuid;
        this.nodes = new HashSet<>();
    }

    public File(String name, long size, int access, UUID uuid, Set<Node> nodes) {
        this.name = name;
        this.size = size;
        this.access = access;
        this.uuid = uuid;
        this.nodes = nodes;
    }

    public String getName() {
        return name;
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

    public Set<Node> getNodes() {
        return nodes;
    }
}
