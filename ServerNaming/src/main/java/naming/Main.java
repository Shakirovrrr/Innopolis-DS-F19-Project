package naming;

import commons.Ports;
import naming.dispatchers.BroadcastDispatcher;
import naming.dispatchers.ClientDispatcher;
import naming.dispatchers.Dispatcher;
import naming.dispatchers.StorageServerDispatcher;

import java.net.UnknownHostException;
import java.nio.file.Path;
import java.util.List;

class Main {
	public static void main(String[] args) {
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

		Dispatcher dispatcher = new Dispatcher(fileManager, nodeStorage, fileStorage);

        ClientDispatcher clientDispatcher = new ClientDispatcher(Ports.PORT_NAMING, dispatcher);
        clientDispatcher.start();

		StorageServerDispatcher storageServerDispatcher = new StorageServerDispatcher(Ports.PORT_INTERNAL, dispatcher);
		storageServerDispatcher.start();

		BroadcastDispatcher broadcastDispatcher = new BroadcastDispatcher();
		broadcastDispatcher.start();
	}
}
