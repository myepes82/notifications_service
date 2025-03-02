package org.vintage.services.imp;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vintage.core.senders.NotificationSender;
import org.vintage.exceptions.ValidationException;
import org.vintage.models.Notification;
import org.vintage.models.NotificationHistory;
import org.vintage.models.UserNotificationPreference;
import org.vintage.models.enums.NotificationChannel;
import org.vintage.models.enums.NotificationStatus;
import org.vintage.services.NotificationHistoryService;
import org.vintage.services.NotificationsService;
import org.vintage.services.NotificationPreferenceService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationsServiceImp implements NotificationsService {

    private static final Logger log = LogManager.getLogger();
    private final NotificationPreferenceService notificationPreferenceService;
    private final NotificationHistoryService notificationHistoryService;

    /**
     * This executor service will be used in the runAync calls
     * this thiking in a high concurrent system and trying to prevent the default use of ForkJoinPool.commonPool()
     * and possible system thread overloading
     */
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Map<NotificationChannel, NotificationSender> notificationSenders;

    public NotificationsServiceImp(
            NotificationPreferenceService notificationPreferenceService,
            NotificationHistoryService notificationHistoryService,
            NotificationSender emailNotificationAdapter) {
        this.notificationPreferenceService = notificationPreferenceService;
        this.notificationHistoryService = notificationHistoryService;
        this.notificationSenders = Map.of(NotificationChannel.EMAIL, emailNotificationAdapter);
    }

    @Override
    public void send(Notification notification) {
        log.info("Sending notification: {} for user {}", notification.getMessage(), notification.getUserId());

        validateNotification(notification);

        List<UserNotificationPreference> userNotificationPreferences = notificationPreferenceService.getPreferences(
                notification.getUserId(),
                notification.getType());

        if (CollectionUtils.isEmpty(userNotificationPreferences)) {
            log.info("No user notification preferences found for notification: {}", notification);
            return;
        }

        List<CompletableFuture<Void>> futures = userNotificationPreferences.stream()
                .map(preference -> CompletableFuture.runAsync(() -> {

                    NotificationChannel channel = preference.getChannel();
                    NotificationSender sender = notificationSenders.get(channel);

                    if (sender == null) {
                        log.warn("No notification sender for channel {}", channel);
                        return;
                    }

                    NotificationHistory history = notificationHistoryService.save(buildSimpleNotificationHistory(notification));
                    history.setChannel(channel);

                    try {
                        sender.sendNotification(notification);
                        history.setStatus(NotificationStatus.SENT);
                        history.setSentAt(LocalDateTime.now());
                    } catch (Exception e) {
                        log.error("Failed to send notification", e);
                        history.setStatus(NotificationStatus.FAILED);
                        history.setErrorMessage(e.getMessage());
                    } finally {
                        log.info("Updating notification history");
                        notificationHistoryService.update(history);
                    }

                }, executorService))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .exceptionally(ex -> {
                    log.error("Failed to send notification", ex);
                    return null;
                }).join();
    }

    private void validateNotification(Notification notification) {
        Objects.requireNonNull(notification, "Notification cannot be null");

        if (StringUtils.isBlank(notification.getMessage())) {
            log.error("Notification message is empty");
            throw new ValidationException("Notification message should not be empty");
        }
        if (Objects.isNull(notification.getType())) {
            log.error("Notification type is empty");
            throw new ValidationException("Notification type should not be empty");
        }
        if (Objects.isNull(notification.getUserId())) {
            log.error("Notification userId is empty");
            throw new ValidationException("Notification userId should not be empty");
        }
    }

    private NotificationHistory buildSimpleNotificationHistory(Notification notification) {
        return NotificationHistory.builder()
                .status(NotificationStatus.PENDING)
                .message(notification.getMessage())
                .userId(notification.getUserId())
                .type(notification.getType())
                .timestamp(Instant.now())
                .build();
    }

}
