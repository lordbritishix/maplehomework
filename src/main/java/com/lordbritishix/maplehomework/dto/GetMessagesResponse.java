package com.lordbritishix.maplehomework.dto;

import com.lordbritishix.maplehomework.model.Message;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class GetMessagesResponse {
    private final UUID conversationId;
    private final List<Message> messages;
}
