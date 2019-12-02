package commons.commands.storage;

public class ConfirmReady extends StorageCommand {
	private boolean agree;

	public ConfirmReady() {
		this.agree = true;
	}

	public ConfirmReady(boolean agree) {
		this.agree = agree;
	}

	public boolean isAgree() {
		return agree;
	}
}
