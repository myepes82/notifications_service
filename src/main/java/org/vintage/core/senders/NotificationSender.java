package org.vintage.core.senders;

import org.vintage.models.Notification;

public interface NotificationSender {
    default void sendNotification(Notification notification) { throw new UnsupportedOperationException("Not supported yet."); };
}
