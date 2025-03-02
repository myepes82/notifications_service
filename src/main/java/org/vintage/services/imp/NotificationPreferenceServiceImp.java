package org.vintage.services.imp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vintage.models.UserNotificationPreference;
import org.vintage.models.enums.NotificationType;
import org.vintage.core.repositories.NotificationPreferencesRepository;
import org.vintage.services.NotificationPreferenceService;

import java.util.List;

public class NotificationPreferenceServiceImp implements NotificationPreferenceService {

    private static final Logger log = LogManager.getLogger();

    private final NotificationPreferencesRepository notificationPreferencesRepository;

    public NotificationPreferenceServiceImp(NotificationPreferencesRepository notificationPreferencesRepository) {
        this.notificationPreferencesRepository = notificationPreferencesRepository;
    }


    @Override
    public List<UserNotificationPreference> getPreferences(Long userId, NotificationType type) {
        log.info("Getting preferences for user: {}, type: {}", userId, type);
        return notificationPreferencesRepository.getPreferences(userId, type);
    }

    @Override
    public void addPreference(UserNotificationPreference preference) {
        log.info("Adding preference: {}", preference);
        notificationPreferencesRepository.addPreference(preference);
    }
}
