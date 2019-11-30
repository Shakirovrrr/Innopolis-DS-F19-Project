package commons.commands.internal;

import java.util.Collection;
import java.util.UUID;

public class FetchFilesAck extends InternalCommand {
	private UUID[] uuids;

	public FetchFilesAck(UUID[] uuids) {
		this.uuids = uuids;
	}

	public FetchFilesAck(Collection<UUID> uuids) {
		this.uuids = uuids.toArray(new UUID[0]);
	}

	public UUID[] getUuids() {
		return uuids;
	}
}
