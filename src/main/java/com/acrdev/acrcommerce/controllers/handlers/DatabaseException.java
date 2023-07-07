package com.acrdev.acrcommerce.controllers.handlers;

public class DatabaseException extends RuntimeException {

    public DatabaseException(String msg) {
        super(msg);
    }
}
