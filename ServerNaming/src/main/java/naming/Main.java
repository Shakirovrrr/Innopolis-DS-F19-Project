package naming;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

class Main {
	public static void main(String[] args) {
		System.out.println("Hello Naming!");

		FileTree fileTree = new FileTree("uniuser");
		Folder root = fileTree.getRoot();

		File fileOne = new File("hello.txx", 124, 12, UUID.randomUUID());
		File fileTwo = new File("hola.txt", 24, 1, UUID.randomUUID());

		Folder folderOne = new Folder("directory");
		Folder folderTwo = new Folder("one");

		root.addFolder(folderOne);
		folderOne.addFolder(folderTwo);
		folderTwo.addFile(fileOne);

		FileManager fileManager = new FileManager(fileTree);

//		/directory/one/hello.txx
		Path path = Paths.get("/");
//		Folder currentFolder = fileManager.getFolder(path);
		File currentFolder = fileManager.getFile(path);

		if (currentFolder != null) {
			System.out.println(currentFolder.getName());
		} else {
			System.out.println(currentFolder);
		}
	}
}
