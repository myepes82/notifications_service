package org.vintage.services.imp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vintage.core.repositories.NotificationHistoryRepository;
import org.vintage.models.NotificationHistory;
import org.vintage.services.NotificationHistoryService;

import java.util.List;
import java.util.Optional;

public class NotificationHistoryServiceImp implements NotificationHistoryService {

    private static final Logger log = LogManager.getLogger();
    private final NotificationHistoryRepository notificationHistoryRepository;

    public NotificationHistoryServiceImp(NotificationHistoryRepository notificationHistoryRepository) {
        this.notificationHistoryRepository = notificationHistoryRepository;
    }

    @Override
    public NotificationHistory save(NotificationHistory notificationHistory) {
        log.info("Saving notification history: {}", notificationHistory);
        return notificationHistoryRepository.save(notificationHistory);
    }

    @Override
    public NotificationHistory update(NotificationHistory notificationHistory) {
        log.info("Updating notification history: {}", notificationHistory);
        return notificationHistoryRepository.update(notificationHistory);
    }

    @Override
    public List<NotificationHistory> getNotificationHistoryByUserId(long userId) {
        log.info("Getting notification history for user: {}", userId);
        return notificationHistoryRepository.getNotificationHistoryByUserId(userId);
    }

}
