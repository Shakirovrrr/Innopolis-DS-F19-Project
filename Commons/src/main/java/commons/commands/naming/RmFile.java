package commons.commands.naming;

public class RmFile extends NamingCommand {
	private String remotePath;

	public RmFile(String remotePath) {
		this.remotePath = remotePath;
	}

	public String getRemotePath() {
		return remotePath;
	}
}
