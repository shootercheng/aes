package com.scd.exception;

/**
 * @author James
 */
public class EncryptException extends RuntimeException {
    public EncryptException(String message, Throwable cause) {
        super(message, cause);
    }
}
