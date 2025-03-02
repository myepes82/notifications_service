package org.vintage.models;

import lombok.*;
import org.vintage.models.enums.NotificationChannel;
import org.vintage.models.enums.NotificationType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserNotificationPreference {
    private String preferenceId;
    private Long userId;
    private NotificationChannel channel;
    private NotificationType notificationType;

    @Override
    public String toString() {
        return "UserNotificationPreference{" +
                "preferenceId=" + preferenceId +
                ", userId=" + userId +
                ", channel=" + channel +
                ", notificationType=" + notificationType +
                '}';
    }
}
