package naming;

import java.net.InetAddress;
import java.util.*;

public class NodesStorage implements Iterable {
    private Map<UUID, Node> nodes;

    public NodesStorage() {
        this.nodes = new HashMap<>();
    }

    public void removeNode(UUID nodeId) {
        nodes.remove(nodeId);
    }

    public void addNode(UUID nodeId, InetAddress publicAddress, InetAddress privateAddress, int portNumber) {
        Node node = new Node(nodeId, publicAddress, privateAddress, portNumber);
        nodes.put(nodeId, node);
    }

    @Override
    public Iterator iterator() {
        return nodes.values().iterator();
    }

    @Override
    public Spliterator spliterator() {
        return nodes.values().spliterator();
    }
}

