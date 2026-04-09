package com.hackerrank.sample.exception;

public class BadResourceRequestException extends RuntimeException {
    public BadResourceRequestException(String msg) {
        super(msg);
    }
}
