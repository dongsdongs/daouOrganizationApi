package daouCodeApiWork.daouCodeApi.exception;

public class ErrorException {
    public final static int BAD_REQUEST = 400;
    public final static int INTERNAL_SERVER_ERROR = 500;

    private final int code;

    public ErrorException(int code) {
        this.code = code;
    }

    public ErrorException(int code, String message) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }
}
