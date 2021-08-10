package daouCodeApiWork.daouCodeApi.exception;

public enum ErrorCodes {
    BAD_REQUEST(400, "요청값이 적절하지 않음"),
    INTERNAL_SERVER_ERROR(500, "내부 서버 오류");

    private int errorCode;
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    ErrorCodes(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
