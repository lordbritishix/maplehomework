package com.lordbritishix.maplehomework.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class Conversation {
    private final UUID conversationId;
    private final List<Message> messages;
}

