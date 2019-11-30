package commons.commands.internal;

import java.net.InetAddress;
import java.util.Collection;
import java.util.UUID;

public class FetchFilesAck extends InternalCommand {
	public static class ToDownload {
		private UUID fileUuid;
		private InetAddress nodeAddress;

		public ToDownload(UUID fileUuid, InetAddress nodeAddress) {
			this.fileUuid = fileUuid;
			this.nodeAddress = nodeAddress;
		}

		public UUID getFileUuid() {
			return fileUuid;
		}

		public InetAddress getNodeAddress() {
			return nodeAddress;
		}
	}

	private UUID[] existedFiles;
	private ToDownload[] filesToDownload;

	public FetchFilesAck(UUID[] existedFiles, ToDownload[] filesToDownload) {
		this.existedFiles = existedFiles;
		this.filesToDownload = filesToDownload;
	}

	public FetchFilesAck(Collection<UUID> existedFiles, ToDownload[] filesToDownload) {
		this.existedFiles = existedFiles.toArray(new UUID[0]);
		this.filesToDownload = filesToDownload;
	}

	public FetchFilesAck(UUID[] existedFiles, Collection<ToDownload> filesToDownload) {
		this.existedFiles = existedFiles;
		this.filesToDownload = filesToDownload.toArray(new ToDownload[0]);
	}

	public FetchFilesAck(Collection<UUID> existedFiles, Collection<ToDownload> filesToDownload) {
		this.existedFiles = existedFiles.toArray(new UUID[0]);
		this.filesToDownload = filesToDownload.toArray(new ToDownload[0]);
	}

	public UUID[] getExistedFiles() {
		return existedFiles;
	}

	public ToDownload[] getFilesToDownload() {
		return filesToDownload;
	}
}
