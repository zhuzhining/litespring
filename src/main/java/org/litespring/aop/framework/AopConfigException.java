package org.litespring.aop.framework;

public class AopConfigException extends RuntimeException{

    public AopConfigException(String message) {
        super(message);
    }

    public AopConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
