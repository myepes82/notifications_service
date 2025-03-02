package org.vintage;

import org.vintage.adapters.data.NotificationHistoryRepositoryAdapter;
import org.vintage.adapters.data.NotificationPreferencesRepositoryAdapter;
import org.vintage.adapters.external.EmailNotificationAdapter;
import org.vintage.core.repositories.NotificationHistoryRepository;
import org.vintage.core.repositories.NotificationPreferencesRepository;
import org.vintage.core.senders.NotificationSender;
import org.vintage.engines.GameEngine;
import org.vintage.engines.SocialEngine;
import org.vintage.models.UserNotificationPreference;
import org.vintage.models.enums.NotificationChannel;
import org.vintage.models.enums.NotificationType;
import org.vintage.services.NotificationHistoryService;
import org.vintage.services.NotificationPreferenceService;
import org.vintage.services.NotificationsService;
import org.vintage.services.imp.NotificationHistoryServiceImp;
import org.vintage.services.imp.NotificationPreferenceServiceImp;
import org.vintage.services.imp.NotificationsServiceImp;

public class Main {
    // Repositories
    private static final NotificationPreferencesRepository preferencesRepository = new NotificationPreferencesRepositoryAdapter();
    private static final NotificationHistoryRepository notificationHistoryRepository = new NotificationHistoryRepositoryAdapter();

    // Senders
    private static final NotificationSender emailNotificationSender = new EmailNotificationAdapter();

    // Services
    private static final NotificationPreferenceService notificationPreferenceService = new NotificationPreferenceServiceImp(preferencesRepository);
    private static final NotificationHistoryService notificationHistoryService = new NotificationHistoryServiceImp(notificationHistoryRepository);

    private static final NotificationsService notificationsService = new NotificationsServiceImp(
            notificationPreferenceService,
            notificationHistoryService,
            emailNotificationSender);



    public static void main(String[] args) {

        // Preferences
        UserNotificationPreference notificationInGamePreference = UserNotificationPreference.builder()
                .userId(1L)
                .channel(NotificationChannel.EMAIL)
                .notificationType(NotificationType.IN_GAME)
                .build();
        UserNotificationPreference notificationSocialPreference = UserNotificationPreference.builder()
                .userId(1L)
                .channel(NotificationChannel.EMAIL)
                .notificationType(NotificationType.SOCIAL)
                .build();

        preferencesRepository.addPreference(notificationInGamePreference);
        preferencesRepository.addPreference(notificationSocialPreference);

        GameEngine gameEngine = new GameEngine(notificationsService);
        SocialEngine socialEngine = new SocialEngine(notificationsService);

        /// Game engine
        ///  You should be able to see the logs about the message that are being send
        gameEngine.itemAcquired(1L, "SwordOfAzeroth");
        gameEngine.playerLeveledUp(1L, 15);
        gameEngine.playerPvp(1L, 1L);


        ///  Social Engine
        ///  You should be able to see the logs about the message that are being send
        socialEngine.friendShipRequest(1L, 2L);
        socialEngine.newFollower(1L);
        socialEngine.friendshipRequestAccepted(1L);
    }
}