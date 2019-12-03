package commons.commands.naming;

public class RmConfirm extends NamingCommand {
	private boolean removeConfirmed;

	public RmConfirm(boolean removeConfirmed) {
		this.removeConfirmed = removeConfirmed;
	}

	public boolean isRemoveConfirmed() {
		return removeConfirmed;
	}
}
