package org.litespring.aop;

public class AopInvocationException extends RuntimeException{

    public AopInvocationException(String message) {
        super(message);
    }

    public AopInvocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
