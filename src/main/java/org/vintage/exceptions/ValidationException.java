package org.vintage.exceptions;

public class ValidationException extends ApplicationException {

    public ValidationException(String message) {
        super(ExceptionType.VALIDATION_ERROR, message);
    }

}
