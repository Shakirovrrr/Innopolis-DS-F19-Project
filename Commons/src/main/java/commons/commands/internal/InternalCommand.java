package commons.commands.internal;

import commons.commands.Command;

import java.util.UUID;

public abstract class InternalCommand extends Command {
    private UUID nodeId;

    public InternalCommand(UUID nodeId) {
        this.nodeId = nodeId;
    }

    public UUID getNodeId() {
        return nodeId;
    }
}
