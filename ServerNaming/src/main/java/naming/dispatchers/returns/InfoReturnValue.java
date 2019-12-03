package naming.dispatchers.returns;

import java.util.Collection;
import java.util.LinkedList;
import java.util.UUID;

public class InfoReturnValue extends ReturnValue {
	long fileSize;
	String accessRights;
	Collection<UUID> nodes;

	public InfoReturnValue(int statusCode) {
		super(statusCode);
		this.fileSize = 0;
		this.accessRights = null;
		this.nodes = new LinkedList<>();
	}

	public InfoReturnValue(int statusCode, long fileSize, String accessRights, Collection<UUID> nodes) {
		super(statusCode);
		this.fileSize = fileSize;
		this.accessRights = accessRights;
		this.nodes = nodes;
	}

	public long getFileSize() {
		return fileSize;
	}

	public String getAccessRights() {
		return accessRights;
	}

	public Collection<UUID> getNodes() {
		return nodes;
	}
}
