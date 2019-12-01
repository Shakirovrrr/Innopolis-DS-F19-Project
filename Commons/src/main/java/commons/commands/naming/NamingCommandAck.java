package commons.commands.naming;

import commons.StatusCodes;

public abstract class NamingCommandAck extends NamingCommand {
	private int statusCode;

	public NamingCommandAck(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}


}
