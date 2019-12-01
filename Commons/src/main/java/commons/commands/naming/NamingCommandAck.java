package commons.commands.naming;

import commons.StatusCodes;

public abstract class NamingCommandAck extends NamingCommand {
//	@Deprecated
//	StatusCodes.Code status;
	int statusCode;
	StatusCodes statusCodes = new StatusCodes();

//	@Deprecated
//	public StatusCodes.Code getStatus() {
//		return status;
//	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getStatusStr(){
		return this.statusCodes.getStatusCode(statusCode);

	}
}
