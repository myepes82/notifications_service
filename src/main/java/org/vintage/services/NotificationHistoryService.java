package org.vintage.services;

import org.vintage.models.NotificationHistory;

import java.util.List;

public interface NotificationHistoryService {
    NotificationHistory save(NotificationHistory notificationHistory);
    NotificationHistory update(NotificationHistory notificationHistory);
    List<NotificationHistory> getNotificationHistoryByUserId(long userId);
}
