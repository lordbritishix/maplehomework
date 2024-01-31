package com.lordbritishix.maplehomework.repository;

import com.lordbritishix.maplehomework.model.Conversation;
import com.lordbritishix.maplehomework.model.Message;
import com.lordbritishix.maplehomework.model.User;
import com.lordbritishix.maplehomework.model.UserConversation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MessageRepositoryIntegrationTest {
    @Autowired
    private MessagesRepository repository;

    @Test
    public void getMessagesReturnsMessages() {
        User user1 = User.builder()
                .userId(UUID.randomUUID())
                .userName("jimquitevis")
                .build();

        User user2 = User.builder()
                .userId(UUID.randomUUID())
                .userName("jacobliam")
                .build();

        User user3 = User.builder()
                .userId(UUID.randomUUID())
                .userName("juliankyle")
                .build();

        // Conversation 1
        UUID conversation1 = UUID.randomUUID();

        Message message1_1 = Message.builder()
                .messageId(UUID.randomUUID())
                .conversationId(conversation1)
                .senderUserId(user1.getUserId())
                .receiverUserId(user2.getUserId())
                .body("Hi there!")
                .build();

        Message message1_2 = Message.builder()
                .messageId(UUID.randomUUID())
                .conversationId(conversation1)
                .senderUserId(user2.getUserId())
                .receiverUserId(user1.getUserId())
                .body("Oh hey, how are you?")
                .build();

        Message message1_3 = Message.builder()
                .messageId(UUID.randomUUID())
                .conversationId(conversation1)
                .senderUserId(user2.getUserId())
                .receiverUserId(user1.getUserId())
                .body("Hey!! Can you answer?")
                .build();

        // Conversation 2
        UUID conversation2 = UUID.randomUUID();

        Message message2_1 = Message.builder()
                .messageId(UUID.randomUUID())
                .conversationId(conversation2)
                .senderUserId(user2.getUserId())
                .receiverUserId(user3.getUserId())
                .body("Hi Julian!")
                .build();

        repository.createUser(user1);
        repository.createUser(user2);
        repository.createUser(user3);

        repository.createConversation(conversation1, user1.getUserId());
        repository.addMessage(message1_1);
        repository.addMessage(message1_2);
        repository.addMessage(message1_3);

        repository.createConversation(conversation2, user2.getUserId());
        repository.addMessage(message2_1);

        // Get messages for conversation 1
        Conversation conversation = repository.getMessages(conversation1).orElseThrow();
        assertEquals(3, conversation.getMessages().size());
        List<Message> messages = conversation.getMessages();

        Message message_recorded_1 = messages.get(0);
        assertEquals("jimquitevis", message_recorded_1.getSenderName());
        assertEquals("jacobliam", message_recorded_1.getReceiverName());
        assertEquals("Hi there!", message_recorded_1.getBody());

        Message message_recorded_2 = messages.get(1);
        assertEquals("jacobliam", message_recorded_2.getSenderName());
        assertEquals("jimquitevis", message_recorded_2.getReceiverName());
        assertEquals("Oh hey, how are you?", message_recorded_2.getBody());

        Message message_recorded_3 = messages.get(2);
        assertEquals("jacobliam", message_recorded_3.getSenderName());
        assertEquals("jimquitevis", message_recorded_3.getReceiverName());
        assertEquals("Hey!! Can you answer?", message_recorded_3.getBody());

        // Get messages for conversation 2
        conversation = repository.getMessages(conversation2).orElseThrow();
        assertEquals(1, conversation.getMessages().size());
        messages = conversation.getMessages();

        message_recorded_1 = messages.get(0);
        assertEquals("jacobliam", message_recorded_1.getSenderName());
        assertEquals("juliankyle", message_recorded_1.getReceiverName());
        assertEquals("Hi Julian!", message_recorded_1.getBody());
    }

    @Test
    public void getConversationsReturnConversations() {
        User user1 = User.builder()
                .userId(UUID.randomUUID())
                .userName("jimquitevis")
                .build();

        User user2 = User.builder()
                .userId(UUID.randomUUID())
                .userName("jacobliam")
                .build();

        User user3 = User.builder()
                .userId(UUID.randomUUID())
                .userName("juliankyle")
                .build();

        User user4 = User.builder()
                .userId(UUID.randomUUID())
                .userName("joaquinjace")
                .build();


        // Conversation 1, created by jimquitevis
        UUID conversation1 = UUID.randomUUID();

        Message message1_1 = Message.builder()
                .messageId(UUID.randomUUID())
                .conversationId(conversation1)
                .senderUserId(user1.getUserId())
                .receiverUserId(user2.getUserId())
                .body("Hi there!")
                .build();

        // Conversation 2, created by jacobliam
        UUID conversation2 = UUID.randomUUID();

        Message message2_1 = Message.builder()
                .messageId(UUID.randomUUID())
                .conversationId(conversation2)
                .senderUserId(user2.getUserId())
                .receiverUserId(user3.getUserId())
                .body("Hi Julian!")
                .build();

        // Conversation 3, created by joaquinjace
        UUID conversation3 = UUID.randomUUID();

        // Write data models to disk
        repository.createUser(user1);
        repository.createUser(user2);
        repository.createUser(user3);
        repository.createUser(user4);

        repository.createConversation(conversation1, user1.getUserId());
        repository.addMessage(message1_1);

        repository.createConversation(conversation2, user2.getUserId());
        repository.addMessage(message2_1);

        repository.createConversation(conversation3, user4.getUserId());

        // Exercise sut and do asserts
        List<UserConversation> conversations = repository.getConversations().orElseThrow();

        assertEquals(3, conversations.size());

        UserConversation userConversation1 = conversations.get(0);
        assertEquals(conversation1, userConversation1.getConversationId());
        assertEquals("jimquitevis", userConversation1.getSenderName());
        assertTrue(userConversation1.isSenderOnline());

        UserConversation userConversation2 = conversations.get(1);
        assertEquals(conversation2, userConversation2.getConversationId());
        assertEquals("jacobliam", userConversation2.getSenderName());
        assertTrue(userConversation2.isSenderOnline());

        UserConversation userConversation3 = conversations.get(2);
        assertEquals(conversation3, userConversation3.getConversationId());
        assertEquals("joaquinjace", userConversation3.getSenderName());
        assertFalse(userConversation3.isSenderOnline());
    }
}
