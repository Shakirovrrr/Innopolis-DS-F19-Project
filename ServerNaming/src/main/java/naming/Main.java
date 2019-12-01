package naming;

import commons.Ports;
import naming.dispatchers.ClientDispatcher;
import naming.dispatchers.Dispatcher;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.PriorityQueue;
import java.util.UUID;

class Main {
	private static class Temp implements Comparable {
		private int a;
		public Temp(int a) {
			this.a = a;
		}

		public int getA() {
			return a;
		}

		public void setA(int a) {
			this.a = a;
		}

		@Override
		public int compareTo(Object o) {
			if (this.getA() == ((Temp) o).getA()) {
				return 0;
			}
			if (this.getA() < ((Temp) o).getA()) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	public static void main(String[] args) throws UnknownHostException {

		Path path = Paths.get("////a/!/garg/");
		System.out.println(path.getParent());
		System.out.println(path.getNameCount());
		System.out.println(path.getFileName());
		Temp a = new Temp(2);
		Temp b = new Temp(3);
		PriorityQueue<Temp> priorityQueue = new PriorityQueue<Temp>();
		priorityQueue.add(a);
		priorityQueue.add(b);
		System.out.println(priorityQueue.peek().getA());
		priorityQueue.remove(b);
		b.setA(1);
		priorityQueue.add(b);
		System.out.println(priorityQueue.peek().getA());
//		FileTree fileTree = new FileTree("uniuser");
//		Folder root = fileTree.getRoot();
//		FileManager fileManager = new FileManager();
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

//		NodeStorage nodeStorage = new NodeStorage();
//		nodeStorage.addNode(UUID.randomUUID(), InetAddress.getByName("192.168.0.1"), InetAddress.getByName("192.168.0.1"), 30);
//		FileStorage fileStorage = new FileStorage();
//		Dispatcher dispatcher = new Dispatcher(fileManager, nodeStorage, fileStorage);
////
//        ClientDispatcher clientDispatcher = new ClientDispatcher(Ports.PORT_NAMING, dispatcher);
//        new Thread(() -> clientDispatcher.start());
//        clientDispatcher.start();

//        while (true) {
//
//        }

	}
}
