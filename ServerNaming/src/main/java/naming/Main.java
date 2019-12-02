package naming;

import commons.Ports;
import naming.dispatchers.ClientDispatcher;
import naming.dispatchers.Dispatcher;
import naming.dispatchers.StorageServerDispatcher;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

class Main {
	public static void main(String[] args) throws UnknownHostException, InterruptedException {
		FileManager fileManager = Dumper.getTree();
		NodeStorage nodeStorage = new NodeStorage();
		FileStorage fileStorage = new FileStorage();

		// No storaged file tree
		if (fileManager == null) {
			fileManager = new FileManager();
			Dumper.dumpTree(fileManager);
		} else {
			List<Path> filesPaths = fileManager.getAllFilesPaths();
			for (Path filePath : filesPaths) {
				File file = fileManager.getFile(filePath);
				fileStorage.addFile(file.getId(), filePath);
			}
		}

//		Folder root = fileManager.getRoot();
//
//		File fileOne = new File("hello.txx", 124, 12, UUID.randomUUID(), false);
//		File fileTwo = new File("hola.txt", 24, 1, UUID.randomUUID(), false);
//
//		Folder folderOne = new Folder("directory");
//		Folder folderTwo = new Folder("one");
//
//		root.addFolder(folderOne);
//		folderOne.addFolder(folderTwo);
//		folderTwo.addFile(fileOne);

//		/directory/one/hello.txx
//		Path path = Paths.get("/hello/.her");
//		System.out.println(path.getParent());
//		System.out.println(path.getFileName());
//		Folder currentFolder = fileManager.getFolder(path);
//		File currentFolder = fileManager.getFile(path);

//		if (currentFolder != null) {
//			System.out.println(currentFolder.getName());
//		} else {
//			System.out.println(currentFolder);
//		}


//		nodeStorage.addNode(UUID.randomUUID(), InetAddress.getByName("192.168.0.1"), InetAddress.getByName("192.168.0.1"), 30);

		Dispatcher dispatcher = new Dispatcher(fileManager, nodeStorage, fileStorage);

        ClientDispatcher clientDispatcher = new ClientDispatcher(Ports.PORT_NAMING, dispatcher);
        clientDispatcher.start();

		StorageServerDispatcher storageServerDispatcher = new StorageServerDispatcher(Ports.PORT_INTERNAL, dispatcher);
		storageServerDispatcher.start();

//		Path path = Paths.get("/de");
//		System.out.println(Paths.get(path.toString(), "23"));

//        while (true) {
//
//        }

	}
}
