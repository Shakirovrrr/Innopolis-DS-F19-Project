package commons.commands.naming;

import commons.StatusCodes;

import java.net.InetAddress;
import java.util.Collection;
import java.util.UUID;

public class PutAck extends NamingCommandAck {
    private InetAddress storageAddress;
    private UUID fileId;
    private Collection<InetAddress> replicaAddresses;

    public PutAck(StatusCodes.Code statusCode, InetAddress storageAddress, UUID fileId, Collection<InetAddress> replicaAddresses) {
        this.status = statusCode;
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
