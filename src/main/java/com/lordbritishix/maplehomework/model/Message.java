package com.lordbritishix.maplehomework.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class Message {
    private final UUID messageId;
    private final UUID conversationId;
    private final UUID senderUserId;
    private final UUID receiverUserId;
    private final String senderName;
    private final String receiverName;
    private final String body;
    private final LocalDateTime sentAt;
}
