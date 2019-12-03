package naming.dispatchers.returns;

public abstract class ReturnValue {
	private int status;

	ReturnValue(int statusCode) {
		this.status = statusCode;
	}

	public int getStatus() {
		return status;
	}
}
