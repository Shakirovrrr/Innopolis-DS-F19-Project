package commons.commands.naming;

public class Cd extends NamingCommand {
	private String remotePath;

	public Cd(String remotePath) {
		this.remotePath = remotePath;
	}

	public String getRemotePath() {
		return remotePath;
	}
}
