package commons;

public class StatusCodes {
    // init, put, touch, get, info, cp, mv, cd, ls, mkdir
    public static enum Code {
        OK,
        FILE_DOES_NOT_EXIST,
        DIRECTORY_DOES_NOT_EXIST,
        FILE_ALREADY_EXISTS,
        DIRECTORY_ALREADY_EXISTS,
        NOT_A_DIRECTORY
    }
}
