package commons.commands.naming;

import java.util.List;

public class LsAck extends NamingCommandAck {
	private List<String> folders;
	private List<String> files;

	public LsAck(int statusCode, List<String> folders, List<String> files) {
		super(statusCode);
		this.folders = folders;
		this.files = files;
	}

	public List<String> getFolders() {
		return folders;
	}

	public List<String> getFiles() {
		return files;
	}
}
