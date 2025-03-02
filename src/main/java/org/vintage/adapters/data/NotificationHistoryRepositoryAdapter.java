package org.vintage.adapters.data;

import org.apache.commons.collections4.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vintage.core.repositories.NotificationHistoryRepository;
import org.vintage.models.NotificationHistory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class NotificationHistoryRepositoryAdapter implements NotificationHistoryRepository {

    private static final Logger log = LogManager.getLogger();
    private final Map<Long, Map<String, NotificationHistory>> notificationsHistory = new ConcurrentHashMap<>();

    @Override
    public NotificationHistory save(NotificationHistory history) {
        String historyId = UUID.randomUUID().toString();
        history.setId(historyId);
        notificationsHistory.computeIfAbsent(history.getUserId(), k -> new ConcurrentHashMap<>())
                .put(historyId, history);
        log.info("Notification history saved: {}", history);
        return history;
    }

    @Override
    public NotificationHistory update(NotificationHistory notificationHistory) {
        Map<String, NotificationHistory> userHistory = notificationsHistory.get(notificationHistory.getUserId());

        if (userHistory != null && userHistory.containsKey(notificationHistory.getId())) {
            userHistory.put(notificationHistory.getId(), notificationHistory);
            log.info("Notification history updated: {}", notificationHistory);
            return notificationHistory;
        }

        log.warn("Notification history could not be updated: {}", notificationHistory);
        return null;
    }

    @Override
    public List<NotificationHistory> getNotificationHistoryByUserId(Long userId) {
        log.info("Retrieving notification history by id: {}", userId);
        Map<String, NotificationHistory>  history = Optional.ofNullable(notificationsHistory.get(userId)).orElse(new ConcurrentHashMap<>());

        if (MapUtils.isEmpty(history)) {
            return new ArrayList<>();
        }

        return new ArrayList<>(history.values());
    }

}
