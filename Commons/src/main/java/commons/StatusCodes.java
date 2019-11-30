package commons;

public class StatusCodes {
	public static final int OK = 0;
	public static final int IS_TOUCHED = 1;
	public static final int FILE_OR_DIRECTORY_DOES_NOT_EXIST = 2;
	public static final int FILE_OR_DIRECTORY_ALREADY_EXISTS = 3;
	public static final int INCORRECT_NAME = 4;
	public static final int NO_NODES_AVAILABLE = 5;
	public static final int UNKNOWN_COMMAND = 6;

	public static final int UPLOAD_FAILED = 11;

	// init, put, touch, get, info, cp, mv, cd, ls, mkdir
	@Deprecated(forRemoval = true)
	public enum Code {
		OK,
		IS_TOUCHED,
		FILE_OR_DIRECTORY_DOES_NOT_EXIST,
		FILE_OR_DIRECTORY_ALREADY_EXISTS,
		INCORRECT_NAME,
		NO_NODES_AVAILABLE,
		UNKNOWN_COMMAND
	}
}
