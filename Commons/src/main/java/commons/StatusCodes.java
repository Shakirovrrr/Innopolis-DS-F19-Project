package commons;

public class StatusCodes {
	public static final int OK = 0;
	public static final int FILE_OR_DIRECTORY_DOES_NOT_EXIST = 1;
	public static final int FILE_OR_DIRECTORY_ALREADY_EXISTS = 2;
	public static final int INCORRECT_NAME = 3;
	public static final int NO_NODES_AVAILABLE = 4;
	public static final int UNKNOWN_COMMAND = 5;

	// init, put, touch, get, info, cp, mv, cd, ls, mkdir
	@Deprecated(forRemoval = true)
	public enum Code {
		OK,
		FILE_OR_DIRECTORY_DOES_NOT_EXIST,
		FILE_OR_DIRECTORY_ALREADY_EXISTS,
		INCORRECT_NAME,
		NO_NODES_AVAILABLE,
		UNKNOWN_COMMAND
	}
}
