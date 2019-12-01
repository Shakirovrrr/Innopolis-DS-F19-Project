package commons.commands.naming;

import commons.StatusCodes;

public abstract class NamingCommandAck extends NamingCommand {
	@Deprecated
	StatusCodes.Code status;
	int statusCode;

	@Deprecated
	public StatusCodes.Code getStatus() {
		return status;
	}

	public int getStatusCode() {
		return statusCode;
	}


}
