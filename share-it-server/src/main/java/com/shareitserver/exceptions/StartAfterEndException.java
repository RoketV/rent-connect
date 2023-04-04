package com.shareitserver.exceptions;

public class StartAfterEndException extends RuntimeException {

    public StartAfterEndException(String message) {
        super(message);
    }
}
