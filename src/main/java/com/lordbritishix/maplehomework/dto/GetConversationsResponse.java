package com.lordbritishix.maplehomework.dto;

import com.lordbritishix.maplehomework.model.UserConversation;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetConversationsResponse {
    private final List<UserConversation> conversations;
}
