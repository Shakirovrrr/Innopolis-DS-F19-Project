package naming;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Node {
    private class NodeAddress {
        private InetAddress publicAddress;
        private InetAddress privateAddress;

        public NodeAddress(InetAddress publicAddress, InetAddress privateAddress) {
            this.publicAddress = publicAddress;
            this.privateAddress = privateAddress;
        }

        public InetAddress getPublicAddress() {
            return publicAddress;
        }

        public InetAddress getPrivateAddress() {
            return privateAddress;
        }
    }

    private UUID nodeId;
    private NodeAddress nodeAddress;
    private Set<UUID> keepingFiles;

    public Node(UUID nodeId, InetAddress publicAddress, InetAddress privateAddress) {
        this.nodeId = nodeId;
        this.nodeAddress = new NodeAddress(publicAddress, privateAddress);
        this.keepingFiles = new HashSet<>();
    }

    public Node(UUID nodeId, InetAddress publicAddress, InetAddress privateAddress, Set<UUID> keepingFiles) {
        this.nodeId = nodeId;
        this.nodeAddress = new NodeAddress(publicAddress, privateAddress);
        this.keepingFiles = keepingFiles;
    }

    public UUID getNodeId() {
        return nodeId;
    }

    public InetAddress getPublicIpAddress() {
        return nodeAddress.getPublicAddress();
    }

    public InetAddress getPrivateIpAddress() {
        return nodeAddress.getPrivateAddress();
    }

    public Set<UUID> getKeepingFiles() {
        return keepingFiles;
    }

    public boolean addKeepingFile(UUID fileId) {
        return keepingFiles.add(fileId);
    }

    public boolean removeKeepingFile(UUID fileId) {
        return keepingFiles.remove(fileId);
    }

    public void removeAllKeepingFiles() {
        keepingFiles = new HashSet<>();
    }
}
