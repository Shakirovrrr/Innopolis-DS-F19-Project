package commons;

public class StatusCodes {
    // init, put, touch, get, info, cp, mv, cd, ls, mkdir
    public static enum Code {
        OK,
        FILE_OR_DIRECTORY_DOES_NOT_EXIST,
//        DIRECTORY_DOES_NOT_EXIST,
        FILE_OR_DIRECTORY_ALREADY_EXISTS,
//        DIRECTORY_ALREADY_EXISTS,
        INCORRECT_NAME,
        NOT_A_DIRECTORY,
        NO_NODES_AVAILABLE,
        UNKNOWN_COMMAND
    }
}
