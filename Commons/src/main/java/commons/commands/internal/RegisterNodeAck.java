package commons.commands.internal;

public class RegisterNodeAck extends InternalCommand {
	private int status;

	public RegisterNodeAck(int statusCode) {
		this.status = statusCode;
	}

	public int getStatus() {
		return status;
	}
}
