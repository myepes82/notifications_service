package org.vintage.exceptions;

public class NotificationNotSentException extends ApplicationException {

    public NotificationNotSentException(String message) {
        super(ExceptionType.INTERNAL_SERVER_ERROR, message);
    }

}
