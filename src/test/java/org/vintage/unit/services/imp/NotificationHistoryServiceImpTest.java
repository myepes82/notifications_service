package org.vintage.unit.services.imp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vintage.core.repositories.NotificationHistoryRepository;
import org.vintage.models.NotificationHistory;
import org.vintage.models.enums.NotificationStatus;
import org.vintage.services.imp.NotificationHistoryServiceImp;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationHistoryServiceImpTest {

    private NotificationHistoryRepository notificationHistoryRepository;
    private NotificationHistoryServiceImp notificationHistoryService;

    @BeforeEach
    void setup() {
        notificationHistoryRepository = mock(NotificationHistoryRepository.class);
        notificationHistoryService = new NotificationHistoryServiceImp(notificationHistoryRepository);
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

        when(notificationHistoryRepository.save(any(NotificationHistory.class))).thenReturn(history);

        // Act
        NotificationHistory savedHistory = notificationHistoryService.save(history);

        // Assert - Verify
        assertNotNull(savedHistory);
        assertEquals(1L, savedHistory.getUserId());
        verify(notificationHistoryRepository).save(history);
    }

    @Test
    void shouldUpdateNotificationHistory() {
        // Arrange
        NotificationHistory history = NotificationHistory.builder()
                .id("1234")
                .userId(1L)
                .message("Updated Message")
                .status(NotificationStatus.SENT)
                .timestamp(Instant.now())
                .build();

        when(notificationHistoryRepository.update(any(NotificationHistory.class))).thenReturn(history);

        // Act
        NotificationHistory updatedHistory = notificationHistoryService.update(history);

        // Assert - Verify
        assertNotNull(updatedHistory);
        assertEquals("Updated Message", updatedHistory.getMessage());
        assertEquals(NotificationStatus.SENT, updatedHistory.getStatus());
        verify(notificationHistoryRepository).update(history);
    }
}
