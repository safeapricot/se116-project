package com.objectville.exception;

public class InvalidMapException extends RuntimeException {
    public InvalidMapException(String message) {
        super(message);
    }
}