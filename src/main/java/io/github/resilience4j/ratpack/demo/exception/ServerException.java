package io.github.resilience4j.ratpack.demo.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

public class ServerException extends RuntimeException {

    private final HttpResponseStatus statusCode;

    public ServerException(HttpResponseStatus statusCode) {
        this.statusCode = statusCode;
    }

    public ServerException(String message, HttpResponseStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public ServerException(String message, Throwable cause, HttpResponseStatus statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public ServerException(Throwable cause, HttpResponseStatus statusCode) {
        super(cause);
        this.statusCode = statusCode;
    }

    public ServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, HttpResponseStatus statusCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.statusCode = statusCode;
    }
}
