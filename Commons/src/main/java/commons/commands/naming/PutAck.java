package commons.commands.naming;

import commons.StatusCodes;
import commons.commands.internal.NodePrivateAddress;
import commons.commands.internal.NodePublicAddress;

import java.net.InetAddress;
import java.util.Collection;
import java.util.UUID;

public class PutAck extends NamingCommandAck {
    private InetAddress storageAddress;
    private UUID fileId;
    private Collection<InetAddress> replicaAddresses;

    public PutAck(int statusCode, InetAddress storageAddress, UUID fileId, Collection<InetAddress> replicaAddresses) {
        super(statusCode);
        this.storageAddress = storageAddress;
        this.fileId = fileId;
        this.replicaAddresses = replicaAddresses;
    }

    public InetAddress getStorageAddress() {
        return storageAddress;
    }

    public UUID getFileId() {
        return fileId;
    }

    public Collection<InetAddress> getReplicaAddresses() {
        return replicaAddresses;
    }
}
