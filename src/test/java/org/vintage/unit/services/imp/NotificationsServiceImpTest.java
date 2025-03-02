package org.vintage.unit.services.imp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vintage.core.senders.NotificationSender;
import org.vintage.exceptions.ValidationException;
import org.vintage.models.Notification;
import org.vintage.models.NotificationHistory;
import org.vintage.models.UserNotificationPreference;
import org.vintage.models.enums.NotificationChannel;
import org.vintage.models.enums.NotificationStatus;
import org.vintage.models.enums.NotificationType;
import org.vintage.services.NotificationHistoryService;
import org.vintage.services.NotificationPreferenceService;
import org.vintage.services.NotificationsService;
import org.vintage.services.imp.NotificationsServiceImp;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class NotificationsServiceImpTest {

    private NotificationPreferenceService notificationPreferenceService;
    private NotificationHistoryService notificationHistoryService;
    private NotificationsService notificationsService;

    // Notification Senders
    private NotificationSender emailNotificationAdapter;

    @BeforeEach()
    void setup() {
        emailNotificationAdapter = mock(NotificationSender.class);
        notificationPreferenceService = mock(NotificationPreferenceService.class);
        notificationHistoryService = mock(NotificationHistoryService.class);

        notificationsService = new NotificationsServiceImp(
                notificationPreferenceService,
                notificationHistoryService,
                emailNotificationAdapter
        );

    }

    @Test
    void shouldThrowExceptionWhenNotificationIsNull() {
        assertThrows(NullPointerException.class, () -> notificationsService.send(null));
    }

    @Test
    void shouldThrowExceptionWhenMessageIsBlank() {
        Notification notification = Notification.builder()
                .userId(1L)
                .type(NotificationType.IN_GAME)
                .message(" ")
                .build();

        assertThrows(ValidationException.class, () -> notificationsService.send(notification));
    }

    @Test
    void shouldThrowExceptionWhenTypeIsNull() {
        Notification notification = Notification.builder()
                .userId(1L)
                .type(null)
                .message("Test message")
                .build();

        assertThrows(ValidationException.class, () -> notificationsService.send(notification));
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNull() {
        Notification notification = Notification.builder()
                .userId(null)
                .type(NotificationType.IN_GAME)
                .message("Test message")
                .build();

        assertThrows(ValidationException.class, () -> notificationsService.send(notification));
    }

    @Test
    void shouldHandleExceptionWhenSenderFails() {
        // Arrange
        Long userId = 1L;
        NotificationType notificationType = NotificationType.IN_GAME;

        Notification notification = Notification.builder()
                .userId(userId)
                .message("Test message")
                .type(notificationType)
                .build();

        NotificationHistory notificationHistory = NotificationHistory.builder()
                .id("12345")
                .userId(userId)
                .type(notificationType)
                .channel(NotificationChannel.EMAIL)
                .status(NotificationStatus.PENDING)
                .build();

        UserNotificationPreference userNotificationPreference = UserNotificationPreference.builder()
                .userId(userId)
                .notificationType(notificationType)
                .channel(NotificationChannel.EMAIL)
                .build();

        when(notificationPreferenceService.getPreferences(userId, notificationType)).thenReturn(List.of(userNotificationPreference));
        when(notificationHistoryService.save(any(NotificationHistory.class))).thenReturn(notificationHistory);

        doThrow(new RuntimeException("Test exception")).when(emailNotificationAdapter).sendNotification(notification);

        // Act
        notificationsService.send(notification);

        // Assert - Verify
        verify(notificationPreferenceService).getPreferences(userId, notificationType);
        verify(notificationHistoryService).save(any(NotificationHistory.class));
        verify(notificationHistoryService).update(argThat(history ->
                history.getStatus() == NotificationStatus.FAILED &&
                        "Test exception".equals(history.getErrorMessage())
        ));
    }

    @Test()
    void shouldSendNotificationWhenUserHasPreferences() {
        // Arrange
        Long userId = 1L;
        NotificationType notificationType = NotificationType.IN_GAME;

        Notification notification = Notification.builder()
                .message("Test message")
                .userId(userId)
                .type(notificationType)
                .build();

        NotificationHistory notificationHistory = NotificationHistory.builder()
                .id("12345")
                .userId(userId)
                .type(notificationType)
                .channel(NotificationChannel.EMAIL)
                .status(NotificationStatus.PENDING)
                .build();

        UserNotificationPreference userNotificationPreference = UserNotificationPreference.builder()
                .userId(userId)
                .notificationType(notificationType)
                .channel(NotificationChannel.EMAIL)
                .build();

        when(notificationPreferenceService.getPreferences(userId, notificationType)).thenReturn(List.of(userNotificationPreference));
        when(notificationHistoryService.save(any(NotificationHistory.class))).thenReturn(notificationHistory);

        // Act
        notificationsService.send(notification);

        // Assert - Verify
        verify(notificationPreferenceService).getPreferences(userId, notificationType);
        verify(emailNotificationAdapter).sendNotification(notification);
        verify(notificationHistoryService).save(any(NotificationHistory.class));
        verify(notificationHistoryService).update(argThat(history -> history.getStatus().equals(NotificationStatus.SENT)));
    }

    @Test()
    void shouldNotSendNotificationWhenUserHasNoPreferences() {
        // Arrange
        Long userId = 1L;
        NotificationType notificationType = NotificationType.IN_GAME;

        Notification notification = Notification.builder()
                .userId(userId)
                .message("Test message")
                .type(notificationType)
                .build();

        when(notificationPreferenceService.getPreferences(userId, notificationType)).thenReturn(Collections.emptyList());

        // Act
        notificationsService.send(notification);

        // Assert - Verify
        verify(notificationPreferenceService).getPreferences(userId, notificationType);
        verifyNoInteractions(emailNotificationAdapter);

    }

    @Test
    void shouldNotSendNotificationWhenNoSenderIsRegistered() {
        // Arrange
        Long userId = 1L;
        NotificationType notificationType = NotificationType.IN_GAME;

        Notification notification = Notification.builder()
                .message("Test message")
                .userId(userId)
                .type(notificationType)
                .build();


        UserNotificationPreference userNotificationPreference = UserNotificationPreference.builder()
                .userId(userId)
                .notificationType(notificationType)
                .channel(NotificationChannel.SMS)
                .build();

        when(notificationPreferenceService.getPreferences(userId, notificationType)).thenReturn(List.of(userNotificationPreference));

        // Act
        notificationsService.send(notification);

        // Assert - Verify
        verify(notificationPreferenceService).getPreferences(userId, notificationType);
        verifyNoInteractions(emailNotificationAdapter);
        verifyNoInteractions(notificationHistoryService);
    }

}

























