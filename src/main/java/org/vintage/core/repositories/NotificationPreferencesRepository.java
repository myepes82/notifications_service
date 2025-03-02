package org.vintage.core.repositories;

import org.vintage.models.UserNotificationPreference;
import org.vintage.models.enums.NotificationType;

import java.util.List;

public interface NotificationPreferencesRepository {
    List<UserNotificationPreference> getPreferences(Long userId, NotificationType type);
    UserNotificationPreference addPreference(UserNotificationPreference preference);
}
