package commons.commands.internal;

import java.net.InetAddress;
import java.util.UUID;

public class RegisterNode extends InternalCommand {
	private UUID nodeId;
	private UUID[] files;
	private InetAddress publicAddress;
	private InetAddress localAddress;

	public RegisterNode(UUID nodeId, UUID[] files, InetAddress publicAddress, InetAddress localAddress) {
		this.nodeId = nodeId;
		this.files = files;
		this.publicAddress = publicAddress;
		this.localAddress = localAddress;
	}

	public UUID getNodeId() {
		return nodeId;
	}

	public UUID[] getFiles() {
		return files;
	}

	public InetAddress getPublicAddress() {
		return publicAddress;
	}

	public InetAddress getLocalAddress() {
		return localAddress;
	}

	public boolean hasFiles() {
		return files != null && files.length > 0;
	}
}
