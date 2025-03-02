package org.vintage.adapters.external;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vintage.core.senders.NotificationSender;
import org.vintage.models.Notification;

public class EmailNotificationAdapter implements NotificationSender {

    private static final Logger log = LogManager.getLogger();

    @Override
    public void sendNotification(Notification notification) {
        log.info("Sending email notification: {}", notification);
        log.info("Notification sent: {}", notification);
    }
}
