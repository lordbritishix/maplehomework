package com.lordbritishix.maplehomework.service;

import com.lordbritishix.maplehomework.dto.GetConversationsResponse;
import com.lordbritishix.maplehomework.dto.GetMessagesResponse;
import com.lordbritishix.maplehomework.model.Conversation;
import com.lordbritishix.maplehomework.model.UserConversation;
import com.lordbritishix.maplehomework.repository.MessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessagesService {
    private final MessagesRepository messagesRepository;

    @Autowired
    public MessagesService(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    public Optional<GetMessagesResponse> getMessages(UUID conversationId) {
        Optional<Conversation> conversation = messagesRepository.getMessages(conversationId);

        return conversation.map(c -> GetMessagesResponse.builder()
                .conversationId(c.getConversationId())
                .messages(c.getMessages())
                .build());
    }

    public Optional<GetConversationsResponse> getConversations() {
        Optional<List<UserConversation>> conversations = messagesRepository.getConversations();

        return conversations.map(c -> GetConversationsResponse.builder().conversations(c).build());
    }
}
