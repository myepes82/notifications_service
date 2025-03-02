package org.vintage.engines;

import org.vintage.models.Notification;
import org.vintage.models.enums.NotificationType;
import org.vintage.services.NotificationsService;

public class SocialEngine {
    private static final String FRIENDSHIP_REQUEST = "New Friendship from user %s";
    private static final String FRIENDSHIP_REQUEST_ACCEPTED = "Friendship request accepted";
    private static final String NEW_FOLLOWER = "New follower";

    private final NotificationsService notificationsService;

    public SocialEngine(NotificationsService notificationsService) {
        this.notificationsService = notificationsService;
    }

    public void friendShipRequest(Long userId, Long requesterId) {
        String message = String.format(FRIENDSHIP_REQUEST, userId);
        wrapAndSendSocialNotification(userId, message);
    }

    public void friendshipRequestAccepted(Long userId) {
        wrapAndSendSocialNotification(userId, FRIENDSHIP_REQUEST_ACCEPTED);
    }

    public void newFollower(Long userId) {
        wrapAndSendSocialNotification(userId, NEW_FOLLOWER);
    }

    private void wrapAndSendSocialNotification(Long userId, String message) {
        Notification notification = Notification.builder()
                .userId(userId)
                .message(message)
                .type(NotificationType.IN_GAME)
                .build();
        notificationsService.send(notification);
    }
}
