package com.lordbritishix.maplehomework.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class User {
    private final UUID userId;
    private final String userName;
    private final LocalDateTime lastInteractionTime;
}
