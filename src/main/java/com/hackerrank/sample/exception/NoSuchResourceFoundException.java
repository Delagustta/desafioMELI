package com.hackerrank.sample.exception;

public class NoSuchResourceFoundException extends RuntimeException {
    public NoSuchResourceFoundException(String msg) {
        super(msg);
    }
}
