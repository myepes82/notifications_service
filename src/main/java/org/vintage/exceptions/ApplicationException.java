package org.vintage.exceptions;

import java.io.Serial;

public abstract class ApplicationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 37676328L;

    private final ExceptionType exceptionType;

    public ApplicationException(ExceptionType exceptionType, String message) {
        super(message);
        this.exceptionType = exceptionType;
    }

    @Override
    public String toString() {
        return String.format("ApplicationException [exceptionType=%s, message=%s]", exceptionType, getMessage());
    }
}
