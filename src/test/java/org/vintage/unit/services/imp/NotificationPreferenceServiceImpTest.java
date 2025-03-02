package org.vintage.unit.services.imp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vintage.core.repositories.NotificationPreferencesRepository;
import org.vintage.models.UserNotificationPreference;
import org.vintage.models.enums.NotificationChannel;
import org.vintage.models.enums.NotificationType;
import org.vintage.services.NotificationPreferenceService;
import org.vintage.services.imp.NotificationPreferenceServiceImp;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationPreferenceServiceImpTest {

    private NotificationPreferencesRepository notificationPreferencesRepository;
    private NotificationPreferenceService service;

    private final long userId = 1L;
    private final NotificationType notificationType = NotificationType.IN_GAME;

    @BeforeEach
    void setUp() {
        notificationPreferencesRepository = mock(NotificationPreferencesRepository.class);
        service = new NotificationPreferenceServiceImp(notificationPreferencesRepository);
    }

    @Test
    void shouldReturnPreferencesForUser() {
        // Arrange
        List<UserNotificationPreference> expectedPreferences = List.of(
                UserNotificationPreference.builder()
                        .userId(userId)
                        .notificationType(notificationType)
                        .channel(NotificationChannel.EMAIL)
                        .build()
        );

        when(notificationPreferencesRepository.getPreferences(userId, notificationType)).thenReturn(expectedPreferences);

        // Act
        List<UserNotificationPreference> result = service.getPreferences(userId, notificationType);

        // Act - Verify
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(notificationPreferencesRepository).getPreferences(userId, notificationType);
    }

    @Test
    void shouldReturnEmptyListWhenNoPreferencesFound() {
        // Arrange
        when(notificationPreferencesRepository.getPreferences(userId, notificationType)).thenReturn(Collections.emptyList());

        // Act
        List<UserNotificationPreference> result = service.getPreferences(userId, notificationType);

        // Assert - verify
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(notificationPreferencesRepository).getPreferences(userId, notificationType);
    }

    @Test
    void shouldAddUserNotificationPreference() {
        // Arrange
        UserNotificationPreference preference = UserNotificationPreference.builder()
                .userId(userId)
                .notificationType(notificationType)
                .channel(NotificationChannel.EMAIL)
                .build();

        // Act
        service.addPreference(preference);

        // Verify
        verify(notificationPreferencesRepository).addPreference(preference);
    }
}
