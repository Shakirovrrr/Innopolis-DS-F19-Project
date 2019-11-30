package naming;

import commons.Ports;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

class Main {
	public static void main(String[] args) {

//		FileTree fileTree = new FileTree("uniuser");
//		Folder root = fileTree.getRoot();
		FileManager fileManager = new FileManager();
		Folder root = fileManager.getRoot();

		File fileOne = new File("hello.txx", 124, 12, UUID.randomUUID(), false);
		File fileTwo = new File("hola.txt", 24, 1, UUID.randomUUID(), false);

		Folder folderOne = new Folder("directory");
		Folder folderTwo = new Folder("one");

		root.addFolder(folderOne);
		folderOne.addFolder(folderTwo);
		folderTwo.addFile(fileOne);

//		/directory/one/hello.txx
		Path path = Paths.get("/hello/.her");
		System.out.println(path.getParent());
		System.out.println(path.getFileName());
//		Folder currentFolder = fileManager.getFolder(path);
//		File currentFolder = fileManager.getFile(path);

//		if (currentFolder != null) {
//			System.out.println(currentFolder.getName());
//		} else {
//			System.out.println(currentFolder);
//		}

//		Dispatcher dispatcher = new Dispatcher(fileManager, null, null);
//
//        ClientDispatcher clientDispatcher = new ClientDispatcher(Ports.PORT_NAMING);
//        new Thread(() -> clientDispatcher.start());
//        clientDispatcher.start();

//        while (true) {
//
//        }

	}
}
