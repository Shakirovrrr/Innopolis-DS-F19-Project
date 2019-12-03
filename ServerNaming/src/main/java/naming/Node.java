package naming;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Node {
	private UUID nodeId;
	private NodeAddress nodeAddress;
	private Set<UUID> keepingFiles;

	public Node(UUID nodeId, InetAddress publicAddress, InetAddress privateAddress) {
		this.nodeId = nodeId;
		this.nodeAddress = new NodeAddress(publicAddress, privateAddress);
		this.keepingFiles = new HashSet<>();
	}

	public Node(Node node) {
		this.nodeId = node.getNodeId();
		this.nodeAddress = new NodeAddress(node.getPublicIpAddress(), node.getPrivateIpAddress());
		this.keepingFiles = node.getKeepingFiles();
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

	public void addKeepingFile(UUID fileId) {
		keepingFiles.add(fileId);
	}

	public void removeKeepingFile(UUID fileId) {
		keepingFiles.remove(fileId);
	}

	public void removeAllKeepingFiles() {
		keepingFiles = new HashSet<>();
	}

	private static class NodeAddress {
		private InetAddress publicAddress;
		private InetAddress privateAddress;

		NodeAddress(InetAddress publicAddress, InetAddress privateAddress) {
			this.publicAddress = publicAddress;
			this.privateAddress = privateAddress;
		}

		InetAddress getPublicAddress() {
			return publicAddress;
		}

		InetAddress getPrivateAddress() {
			return privateAddress;
		}
	}
}
