package org.vintage.unit.adapters.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vintage.adapters.data.NotificationPreferencesRepositoryAdapter;
import org.vintage.models.UserNotificationPreference;
import org.vintage.models.enums.NotificationType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotificationPreferencesRepositoryAdapterTest {

    private NotificationPreferencesRepositoryAdapter repository;

    @BeforeEach
    void setup() {
        repository = new NotificationPreferencesRepositoryAdapter();
    }

    @Test
    void shouldReturnEmptyListWhenNoPreferencesExist() {
        List<UserNotificationPreference> preferences = repository.getPreferences(1L, NotificationType.IN_GAME);
        assertNotNull(preferences);
        assertTrue(preferences.isEmpty());
    }

    @Test
    void shouldAddUserNotificationPreference() {
        // Arrange
        UserNotificationPreference preference = UserNotificationPreference.builder()
                .userId(1L)
                .notificationType(NotificationType.IN_GAME)
                .build();

        // Act
        UserNotificationPreference savedPreference = repository.addPreference(preference);
        List<UserNotificationPreference> preferences = repository.getPreferences(1L, NotificationType.IN_GAME);

        // Assert
        assertNotNull(savedPreference.getPreferenceId());
        assertFalse(preferences.isEmpty());
        assertEquals(1, preferences.size());
    }

    @Test
    void shouldStoreMultiplePreferencesForSameUser() {
        // Arrange
        UserNotificationPreference preference1 = UserNotificationPreference.builder()
                .userId(1L)
                .notificationType(NotificationType.IN_GAME)
                .build();

        UserNotificationPreference preference2 = UserNotificationPreference.builder()
                .userId(1L)
                .notificationType(NotificationType.SOCIAL)
                .build();

        // Act
        repository.addPreference(preference1);
        repository.addPreference(preference2);
        List<UserNotificationPreference> preferences = repository.getPreferences(1L, NotificationType.IN_GAME);

        // Assert
        assertEquals(2, preferences.size());
    }

    @Test
    void shouldGenerateUniqueIdsForPreferences() {
        // Arrange
        UserNotificationPreference preference1 = UserNotificationPreference.builder()
                .userId(1L)
                .notificationType(NotificationType.IN_GAME)
                .build();

        UserNotificationPreference preference2 = UserNotificationPreference.builder()
                .userId(1L)
                .notificationType(NotificationType.SOCIAL)
                .build();

        // Act
        UserNotificationPreference saved1 = repository.addPreference(preference1);
        UserNotificationPreference saved2 = repository.addPreference(preference2);

        // Assert
        assertNotEquals(saved1.getPreferenceId(), saved2.getPreferenceId());
    }
}
