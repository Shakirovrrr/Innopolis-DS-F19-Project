package naming;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Node {
    private class NodeAddress {
        private InetAddress publicAddress;
        private InetAddress privateAddress;
        private int portNumber;

        public NodeAddress(InetAddress publicAddress, InetAddress privateAddress, int portNumber) {
            this.publicAddress = publicAddress;
            this.privateAddress = privateAddress;
            this.portNumber = portNumber;
        }

        public InetAddress getPublicAddress() {
            return publicAddress;
        }

        public InetAddress getPrivateAddress() {
            return privateAddress;
        }

        public int getPortNumber() {
            return portNumber;
        }
    }

    private UUID nodeId;
    private NodeAddress nodeAddress;
    private Set<UUID> keepingFiles;

    public Node(UUID nodeId, InetAddress publicAddress, InetAddress privateAddress, int portNumber) {
        this.nodeId = nodeId;
        this.nodeAddress = new NodeAddress(publicAddress, privateAddress, portNumber);
        this.keepingFiles = new HashSet<>();
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

    public int getPortNumber() {
        return nodeAddress.getPortNumber();
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
}
