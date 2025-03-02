package org.vintage.engines;

import org.vintage.models.Notification;
import org.vintage.models.enums.NotificationType;
import org.vintage.services.NotificationsService;

public class GameEngine {
    private static final String LEVEL_UP = "Congratulations! You've reached level %s!";
    private static final String ITEM_ACQUIRED = "You've acquired the legendary %s";
    private static final String CHALLENGE_COMPLETED = "You've challenged the legendary %s";
    private static final String PVP = "You've pvp with user %s";

    private final NotificationsService notificationsService;


    public GameEngine(NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
    }

    public void playerLeveledUp(Long userId, Integer level) {
        String message = String.format(LEVEL_UP, level);
        wrapAndSendInGameNotification(userId, message);
    }

    public void playerChallengeCompleted(Long userId, String challenge) {
        String message = String.format(CHALLENGE_COMPLETED, challenge);
        wrapAndSendInGameNotification(userId, message);
    }

    public void playerPvp(Long userId, Long userPvp) {
        String message = String.format(PVP, userPvp);
        wrapAndSendInGameNotification(userId, message);
    }

    public void itemAcquired(Long userId, String userAcquired) {
        String message = String.format(ITEM_ACQUIRED, userAcquired);
       wrapAndSendInGameNotification(userId, message);
    }

    private void wrapAndSendInGameNotification(Long userId, String message) {
        Notification notification = Notification.builder()
                .userId(userId)
                .message(message)
                .type(NotificationType.IN_GAME)
                .build();
        notificationsService.send(notification);
    }
}
