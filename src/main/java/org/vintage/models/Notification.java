package org.vintage.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.vintage.models.enums.NotificationChannel;
import org.vintage.models.enums.NotificationType;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Notification {
    private Long userId;
    private NotificationType type;
    private String message;

    @Override
    public String toString() {
        return "Notification{" +
                "userId=" + userId +
                ", type=" + type +
                ", message='" + message + '\'' +
                '}';
    }
}
