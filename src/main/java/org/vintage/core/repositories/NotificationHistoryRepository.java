package org.vintage.core.repositories;

import org.vintage.models.NotificationHistory;

import java.util.List;

public interface NotificationHistoryRepository {
    NotificationHistory save(NotificationHistory notificationHistory);
    NotificationHistory update(NotificationHistory notificationHistory);
    List<NotificationHistory> getNotificationHistoryByUserId(Long userId);
}
