package commons.commands.internal;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Collection;
import java.util.UUID;

public class FetchFilesAck extends InternalCommand {
	public static class ToDownload implements Serializable {
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

	private int status;
	private UUID[] existedFiles;
	private ToDownload[] filesToDownload;

	public FetchFilesAck(int statusCode, UUID[] existedFiles, ToDownload[] filesToDownload) {
		this.status = statusCode;
		this.existedFiles = existedFiles;
		this.filesToDownload = filesToDownload;
	}

	public FetchFilesAck(int statusCode, Collection<UUID> existedFiles, ToDownload[] filesToDownload) {
		this.status = statusCode;
		this.existedFiles = existedFiles.toArray(new UUID[0]);
		this.filesToDownload = filesToDownload;
	}

	public FetchFilesAck(int statusCode, UUID[] existedFiles, Collection<ToDownload> filesToDownload) {
		this.status = statusCode;
		this.existedFiles = existedFiles;
		this.filesToDownload = filesToDownload.toArray(new ToDownload[0]);
	}

	public FetchFilesAck(int statusCode, Collection<UUID> existedFiles, Collection<ToDownload> filesToDownload) {
		this.status = statusCode;
		this.existedFiles = existedFiles.toArray(new UUID[0]);
		this.filesToDownload = filesToDownload.toArray(new ToDownload[0]);
	}

	public FetchFilesAck(int statusCode) {
		this.status = statusCode;
		this.existedFiles = null;
		this.filesToDownload = null;
	}

	public int getStatus() {
		return status;
	}

	public UUID[] getExistedFiles() {
		return existedFiles;
	}

	public ToDownload[] getFilesToDownload() {
		return filesToDownload;
	}
}
