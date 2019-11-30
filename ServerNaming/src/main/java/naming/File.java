package naming;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class File {
    private String name;
    private long size;
    private int access;
    private UUID uuid;
    private boolean isTouched;
//    private Set<UUID> nodes;

    public File(String name, UUID uuid, boolean isTouched) {
        this.name = name;
        this.size = 0;
        this.access = 0;
        this.uuid = uuid;
        this.isTouched = isTouched;
//        this.nodes = new HashSet<>();
    }

    public File(String name, long size, int access, UUID uuid, boolean isTouched) {
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

    public int getAccess() {
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

    public void setAccess(int access) {
        this.access = access;
    }

//    public void addNode(UUID nodeId) {
//        nodes.add(nodeId);
//    }

//    public void removeNode(UUID nodeId) {
//        nodes.remove(nodeId);
//    }
}
