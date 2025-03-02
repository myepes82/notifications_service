package org.vintage.models.enums;

import lombok.Getter;

@Getter
public enum NotificationType {
    IN_GAME("In Game event"),
    SOCIAL("Social event"),;

    private String fullName;

    NotificationType(String fullName) {}
}
