package storage;

class CouldNotStartException extends Exception {
	CouldNotStartException(String message, Throwable cause) {
		super(message, cause);
	}

	public CouldNotStartException(String message) {
		super(message);
	}
}
