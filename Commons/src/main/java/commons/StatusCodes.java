package commons;

public class StatusCodes {
	public static final int OK = 0;
	public static final int FILE_DOES_NOT_EXIST = 1;
	public static final int DIRECTORY_DOES_NOT_EXIST = 2;
	public static final int FILE_ALREADY_EXISTS = 3;
	public static final int DIRECTORY_ALREADY_EXISTS = 4;

	// init, put, touch, get, info, cp, mv, cd, ls, mkdir
	@Deprecated(forRemoval = true)
	public enum Code {
		OK,
		FILE_DOES_NOT_EXIST,
		DIRECTORY_DOES_NOT_EXIST,
		FILE_ALREADY_EXISTS,
		DIRECTORY_ALREADY_EXISTS
	}
}
