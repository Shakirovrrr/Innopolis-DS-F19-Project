package commons.commands.internal;

import java.util.UUID;

public class Heartbeat extends InternalCommand {
    private UUID nodeId;

    public Heartbeat(UUID nodeId) {
        this.nodeId = nodeId;
    }

    public UUID getNodeId() {
        return nodeId;
    }
}
