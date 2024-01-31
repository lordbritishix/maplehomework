package com.lordbritishix.maplehomework.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserConversation {
    private final UUID conversationId;
    private final String senderName;
    private final boolean isSenderOnline;
}
