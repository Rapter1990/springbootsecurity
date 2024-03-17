package com.springboot.springbootsecurity.admin.exception;

import java.io.Serial;

public class AdminAlreadyExistException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -8596955790221338007L;

    private static final String DEFAULT_MESSAGE = """
            Admin already exist!
            """;

    public AdminAlreadyExistException() {
        super(DEFAULT_MESSAGE);
    }

    public AdminAlreadyExistException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}
