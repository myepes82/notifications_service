package org.vintage.adapters.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vintage.core.repositories.NotificationPreferencesRepository;
import org.vintage.models.UserNotificationPreference;
import org.vintage.models.enums.NotificationType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class NotificationPreferencesRepositoryAdapter implements NotificationPreferencesRepository {

    private static final Logger log = LogManager.getLogger();

    private final Map<Long, List<UserNotificationPreference>> notificationPreferences = new ConcurrentHashMap<>();

    public NotificationPreferencesRepositoryAdapter() {}

    @Override
    public List<UserNotificationPreference> getPreferences(Long userId, NotificationType type) {
        log.info("Retrieving preferences for  user {}", userId);
        return Optional.ofNullable(notificationPreferences.get(userId)).orElseGet(ArrayList::new);
    }

    @Override
    public UserNotificationPreference addPreference(UserNotificationPreference preference) {
        log.info("Saving user notification preference {}", preference);
        String id = UUID.randomUUID().toString();
        preference.setPreferenceId(id);
        notificationPreferences.computeIfAbsent(preference.getUserId(), k -> new ArrayList<>()).add(preference);
        log.info("Saved user notification preference {}", preference);
        return preference;
    }
}
