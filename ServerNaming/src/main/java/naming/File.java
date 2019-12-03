package naming;

import java.util.UUID;

public class File {
	private String name;
	private long size;
	private String access;
	private UUID uuid;
	private boolean isTouched;

	public File(String name, long size, String access, UUID uuid, boolean isTouched) {
		this.name = name;
		this.size = size;
		this.access = access;
		this.uuid = uuid;
		this.isTouched = isTouched;
	}

	public String getName() {
		return name;
	}

	public long getSize() {
		return size;
	}

	public String getAccess() {
		return access;
	}

	public UUID getId() {
		return uuid;
	}

	public boolean getIsTouched() {
		return isTouched;
	}

}
