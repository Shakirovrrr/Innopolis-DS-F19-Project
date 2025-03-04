package naming;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Folder {
	private String name;
	private Map<String, Folder> folders;
	private Map<String, File> files;

	public Folder(String name) {
		this.name = name;
		this.folders = new HashMap<>();
		this.files = new HashMap<>();
	}

	public Folder(String name, Map<String, Folder> folders, Map<String, File> files) {
		this.name = name;
		this.folders = folders;
		this.files = files;
	}

	public String getName() {
		return name;
	}

	public List<Folder> getFolders() {
		return new LinkedList<>(folders.values());
	}

	public List<File> getFiles() {
		return new LinkedList<>(files.values());
	}

	Folder getFolder(String name) {
		return folders.get(name);
	}

	File getFile(String name) {
		return files.get(name);
	}

	public void addFolder(Folder folder) {
		String folderName = folder.getName();
		if (folderExists(folderName)) {
			return;
		}
		folders.put(folderName, folder);

	}

	void addFile(File file) {
		String fileName = file.getName();
		if (fileExists(fileName)) {
			return;
		}
		files.put(fileName, file);

	}

	boolean removeFile(String fileName) {
		if (!fileExists(fileName)) {
			return false;
		}
		files.remove(fileName);

		return true;
	}

	public boolean removeFolder(String folderName) {
		if (!folderExists(folderName)) {
			return false;
		}
		folders.remove(folderName);

		return true;
	}

	public boolean folderExists(String folderName) {
		return folders.containsKey(folderName);
	}

	boolean fileExists(String fileName) {
		return files.containsKey(fileName);
	}
}
