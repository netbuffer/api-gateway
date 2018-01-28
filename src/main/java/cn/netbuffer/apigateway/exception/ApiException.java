package cn.netbuffer.apigateway.exception;

public class ApiException extends RuntimeException {
    public ApiException() {
    }

    public ApiException(String err) {
        super(err);
    }
}
