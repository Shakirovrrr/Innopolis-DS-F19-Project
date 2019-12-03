package commons.commands.naming;

public class MkDir extends NamingCommand {
	private String remotePath;

	public MkDir(String remotePath) {
		this.remotePath = remotePath;
	}

	public String getRemotePath() {
		return remotePath;
	}
}
