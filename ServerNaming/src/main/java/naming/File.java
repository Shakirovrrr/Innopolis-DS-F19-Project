package naming;

import com.sun.nio.sctp.AbstractNotificationHandler;
import naming.dispatchers.Constants;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class File {
    private String name;
    private long size;
    private String access;
    private UUID uuid;
    private boolean isTouched;
//    private Set<UUID> nodes;

    public File(String name, UUID uuid, boolean isTouched) {
        this.name = name;
        this.size = 0;
        this.access = Constants.DEFAULT_RIGHTS;
        this.uuid = uuid;
        this.isTouched = isTouched;
//        this.nodes = new HashSet<>();
    }

    public File(String name, long size, String access, UUID uuid, boolean isTouched) {
        this.name = name;
        this.size = size;
        this.access = access;
        this.uuid = uuid;
        this.isTouched = isTouched;
//        this.nodes = new HashSet<>();
    }

//    public File(String name, long size, int access, UUID uuid, boolean isTouched, Set<UUID> nodes) {
//        this.name = name;
//        this.size = size;
//        this.access = access;
//        this.uuid = uuid;
//        this.nodes = nodes;
//    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public String getAccess() {
        return access;
    }

    public UUID getId() {
        return uuid;
    }

    public boolean getIsTouched() {
        return isTouched;
    }

//    public Set<UUID> getNodes() {
//        return nodes;
//    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setAccess(String access) {
        this.access = access;
    }

//    public void addNode(UUID nodeId) {
//        nodes.add(nodeId);
//    }

//    public void removeNode(UUID nodeId) {
//        nodes.remove(nodeId);
//    }
}
