package naming.dispatchers.returns;

import commons.commands.internal.FetchFilesAck;

import java.util.Collection;
import java.util.UUID;

public class FetchFilesReturnValue extends ReturnValue {
    private Collection<UUID> existedFiles;
    private Collection<FetchFilesAck.ToDownload> filesToDownload;

    public FetchFilesReturnValue(int statusCode, Collection<UUID> existedFiles, Collection<FetchFilesAck.ToDownload> filesToDownload) {
        super(statusCode);
        this.existedFiles = existedFiles;
        this.filesToDownload = filesToDownload;
    }

    public Collection<UUID> getExistedFiles() {
        return existedFiles;
    }

    public Collection<FetchFilesAck.ToDownload> getFilesToDownload() {
        return filesToDownload;
    }
}
