package commons.commands.naming;


public class PutFile extends NamingCommand {
	private String remotePath;
	private String rights;
	private long size;

	public PutFile(String remotePath, String rights, long size) {
		this.remotePath = remotePath;
		this.rights = rights;
		this.size = size;
	}

	public PutFile(String remotePath, long size) {
		this.rights = "110";
		this.remotePath = remotePath;
		this.size = size;
	}

	public String getRemotePath() {
		return remotePath;
	}

	public String getRights() {
		return rights;
	}

	public long getSize() {
		return size;
	}
}
