package commons.commands.naming;

public class MvFile extends NamingCommand {
	private String fromPath;
	private String toPath;

	public MvFile(String fromPath, String toPath) {
		this.fromPath = fromPath;
		this.toPath = toPath;
	}

	public String getFromPath() {
		return fromPath;
	}

	public String getToPath() {
		return toPath;
	}
}
