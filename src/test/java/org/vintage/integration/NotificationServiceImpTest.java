package org.vintage.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vintage.adapters.data.NotificationHistoryRepositoryAdapter;
import org.vintage.adapters.data.NotificationPreferencesRepositoryAdapter;
import org.vintage.adapters.external.EmailNotificationAdapter;
import org.vintage.core.repositories.NotificationHistoryRepository;
import org.vintage.core.repositories.NotificationPreferencesRepository;
import org.vintage.core.senders.NotificationSender;
import org.vintage.models.Notification;
import org.vintage.models.NotificationHistory;
import org.vintage.models.UserNotificationPreference;
import org.vintage.models.enums.NotificationChannel;
import org.vintage.models.enums.NotificationStatus;
import org.vintage.models.enums.NotificationType;
import org.vintage.services.NotificationHistoryService;
import org.vintage.services.NotificationPreferenceService;
import org.vintage.services.NotificationsService;
import org.vintage.services.imp.NotificationHistoryServiceImp;
import org.vintage.services.imp.NotificationPreferenceServiceImp;
import org.vintage.services.imp.NotificationsServiceImp;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationServiceImpTest {

    // Repositories
    private final NotificationPreferencesRepository preferencesRepository = new NotificationPreferencesRepositoryAdapter();
    private final NotificationHistoryRepository notificationHistoryRepository = new NotificationHistoryRepositoryAdapter();

    // Senders
    private final NotificationSender emailNotificationSender = new EmailNotificationAdapter();

    // Services
    private final NotificationPreferenceService notificationPreferenceService = new NotificationPreferenceServiceImp(preferencesRepository);
    private final NotificationHistoryService notificationHistoryService = new NotificationHistoryServiceImp(notificationHistoryRepository);

    private final NotificationsService notificationsService = new NotificationsServiceImp(
            notificationPreferenceService,
            notificationHistoryService,
            emailNotificationSender);


    @BeforeEach()
    void init() {
        Long userId = 1L;

        UserNotificationPreference userPreference = UserNotificationPreference.builder()
                .userId(userId)
                .notificationType(NotificationType.IN_GAME)
                .channel(NotificationChannel.EMAIL)
                .build();

        preferencesRepository.addPreference(userPreference);
    }

    @Test
    void sendNotification() {
        String testMessage = "Test message from integration";
        long userId = 1L;

        Notification notification = Notification.builder()
                .userId(userId)
                .message(testMessage)
                .type(NotificationType.IN_GAME)
                .build();

        notificationsService.send(notification);


        List<NotificationHistory> history = notificationHistoryService.getNotificationHistoryByUserId(userId);
        NotificationHistory history1 = history.get(0);

        assertEquals(NotificationStatus.SENT, history1.getStatus());
        assertFalse(history.isEmpty());
    }

}





















