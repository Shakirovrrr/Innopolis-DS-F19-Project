package naming;

import java.net.InetAddress;
import java.util.*;

public class NodeStorage implements Iterable {
    private Map<UUID, Node> nodes;

    public NodeStorage() {
        this.nodes = new HashMap<>();
    }

    public void removeNode(UUID nodeId) {
        nodes.remove(nodeId);
    }

    public void addNode(Node node) {
        nodes.put(node.getNodeId(), node);
    }

    public void addNode(UUID nodeId, InetAddress publicAddress, InetAddress privateAddress) {
        Node node = new Node(nodeId, publicAddress, privateAddress);
        nodes.put(nodeId, node);
    }

    public Node getNode(UUID nodeId) {
        return nodes.get(nodeId);
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.values().iterator();
    }

    public List<Node> getNodes() {
        return new LinkedList<>(nodes.values());
    }
}

