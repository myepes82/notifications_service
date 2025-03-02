package org.vintage.services;

import org.vintage.models.UserNotificationPreference;
import org.vintage.models.enums.NotificationType;

import java.util.List;

public interface NotificationPreferenceService {
    default boolean isEnabled(Long userId, NotificationType type) { throw new UnsupportedOperationException("Not supported yet."); };
    default List<UserNotificationPreference> getPreferences(Long userId, NotificationType type) { throw new UnsupportedOperationException("Not supported yet."); };
    default void addPreference(UserNotificationPreference preference) { throw new UnsupportedOperationException("Not supported yet."); };
}
