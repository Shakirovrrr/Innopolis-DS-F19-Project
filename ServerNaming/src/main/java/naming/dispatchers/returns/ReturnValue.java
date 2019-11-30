package naming.dispatchers.returns;

import commons.StatusCodes;

public abstract class ReturnValue {
    private StatusCodes.Code status;

    ReturnValue(StatusCodes.Code statusCode) {
        this.status = statusCode;
    }

    public StatusCodes.Code getStatus() {
        return status;
    }
}
