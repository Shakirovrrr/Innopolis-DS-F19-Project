package commons.commands.naming;

public class InfoFile extends NamingCommand {
	private String remotePath;

	public InfoFile(String remotePath) {
		this.remotePath = remotePath;
	}

	public String getRemotePath() {
		return remotePath;
	}
}
