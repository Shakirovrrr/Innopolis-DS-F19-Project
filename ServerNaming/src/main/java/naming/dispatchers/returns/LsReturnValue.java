package naming.dispatchers.returns;

import java.util.List;

public class LsReturnValue extends ReturnValue {
	private List<String> folders;
	private List<String> files;

	public LsReturnValue(int statusCode, List<String> folders, List<String> files) {
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
