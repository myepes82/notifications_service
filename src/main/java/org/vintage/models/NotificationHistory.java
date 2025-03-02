package org.vintage.models;

import lombok.*;
import org.vintage.models.enums.NotificationChannel;
import org.vintage.models.enums.NotificationStatus;
import org.vintage.models.enums.NotificationType;

import java.time.Instant;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationHistory {

    private String id;

    @Builder.Default
    private NotificationStatus status = NotificationStatus.PENDING;
    private NotificationType type;
    private NotificationChannel channel;

    private Long userId;
    private String message;

    private Instant timestamp;
    private LocalDateTime sentAt;
    private String errorMessage;

    @Override
    public String toString() {
        return "NotificationHistory{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", channel=" + channel +
                ", userId=" + userId +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", sentAt=" + sentAt +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
