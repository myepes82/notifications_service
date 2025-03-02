package org.vintage.unit.adapters.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vintage.adapters.data.NotificationHistoryRepositoryAdapter;
import org.vintage.models.NotificationHistory;
import org.vintage.models.enums.NotificationStatus;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class NotificationHistoryRepositoryAdapterTest {

    private NotificationHistoryRepositoryAdapter repository;

    @BeforeEach
    void setup() {
        repository = new NotificationHistoryRepositoryAdapter();
    }

    @Test
    void shouldSaveNotificationHistory() {
        // Arrange
        NotificationHistory history = NotificationHistory.builder()
                .userId(1L)
                .message("Test Message")
                .status(NotificationStatus.PENDING)
                .timestamp(Instant.now())
                .build();

        // Act
        NotificationHistory savedHistory = repository.save(history);

        // Assert
        assertNotNull(savedHistory.getId());
        assertEquals(1L, savedHistory.getUserId());
        assertEquals("Test Message", savedHistory.getMessage());
        assertEquals(NotificationStatus.PENDING, savedHistory.getStatus());
    }

    @Test
    void shouldUpdateExistingNotificationHistory() {
        // Arrange
        NotificationHistory history = NotificationHistory.builder()
                .userId(1L)
                .message("Test Message")
                .status(NotificationStatus.PENDING)
                .timestamp(Instant.now())
                .build();

        NotificationHistory savedHistory = repository.save(history);
        savedHistory.setStatus(NotificationStatus.SENT);

        // Act
        NotificationHistory updatedHistory = repository.update(savedHistory);

        // Assert
        assertNotNull(updatedHistory);
        assertEquals(NotificationStatus.SENT, updatedHistory.getStatus());
    }

    @Test
    void shouldNotUpdateNonExistingNotificationHistory() {
        // Arrange
        NotificationHistory history = NotificationHistory.builder()
                .id("non-existing-id")
                .userId(1L)
                .message("Test Message")
                .status(NotificationStatus.PENDING)
                .timestamp(Instant.now())
                .build();

        // Act
        NotificationHistory updatedHistory = repository.update(history);

        // Assert
        assertNull(updatedHistory);
    }
}
