package commons.commands.internal;

import java.util.UUID;

public class Heartbeat extends InternalCommand {
    public Heartbeat(UUID nodeId) {
        super(nodeId);
    }
}
