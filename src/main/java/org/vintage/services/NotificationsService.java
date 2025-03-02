package org.vintage.services;

import org.vintage.models.Notification;

public interface NotificationsService {
    default void send(Notification notification) { throw new UnsupportedOperationException("Not supported yet."); };
}
