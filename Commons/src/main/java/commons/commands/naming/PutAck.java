package commons.commands.naming;

import commons.StatusCodes;
import commons.commands.internal.NodePrivateAddress;
import commons.commands.internal.NodePublicAddress;

import java.net.InetAddress;
import java.util.Collection;
import java.util.UUID;

public class PutAck extends NamingCommandAck {
    private NodePublicAddress storageAddress;
    private UUID fileId;
    private Collection<NodePrivateAddress> replicaAddresses;

    public PutAck(StatusCodes.Code statusCode, NodePublicAddress storageAddress, UUID fileId, Collection<NodePrivateAddress> replicaAddresses) {
        this.status = statusCode;
        this.storageAddress = storageAddress;
        this.fileId = fileId;
        this.replicaAddresses = replicaAddresses;
    }

    public NodePublicAddress getStorageAddress() {
        return storageAddress;
    }

    public UUID getFileId() {
        return fileId;
    }

    public Collection<NodePrivateAddress> getReplicaAddresses() {
        return replicaAddresses;
    }
}
