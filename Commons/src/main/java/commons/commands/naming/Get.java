package commons.commands.naming;

public class Get extends NamingCommand {
	private String fromPath;

	public Get(String fromPath) {
		this.fromPath = fromPath;
	}

	public String getFromPath() {
		return fromPath;
	}
}
