package com.lordbritishix.maplehomework.repository;

import com.lordbritishix.maplehomework.model.Conversation;
import com.lordbritishix.maplehomework.model.Message;
import com.lordbritishix.maplehomework.model.User;
import com.lordbritishix.maplehomework.model.UserConversation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class MessagesRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public MessagesRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    void createConversation(UUID conversationId, UUID userId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("conversation_id", conversationId);
        parameters.put("user_id", userId);

        String sql = "INSERT INTO conversations (conversation_id, sender_user_id) VALUES (:conversation_id::uuid, :user_id::uuid) ON CONFLICT (conversation_id) DO NOTHING";
        jdbcTemplate.update(sql, parameters);
    }

    void createUser(User user) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("user_id", user.getUserId());
        parameters.put("username", user.getUserName());

        String sql = "INSERT INTO users (user_id, username) VALUES (:user_id::uuid, :username) " +
                "ON CONFLICT (user_id) DO NOTHING ";
        jdbcTemplate.update(sql, parameters);
    }

    @Transactional
    public void addMessage(Message message) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message_id", message.getMessageId());
        parameters.put("conversation_id", message.getConversationId());
        parameters.put("sender_user_id", message.getSenderUserId());
        parameters.put("receiver_user_id", message.getReceiverUserId());
        parameters.put("body", message.getBody());

        // Update message
        String sql = "INSERT INTO messages (message_id, conversation_id, sender_user_id, receiver_user_id, body) " +
                "VALUES (:message_id::uuid, :conversation_id::uuid, :sender_user_id::uuid, :receiver_user_id::uuid, :body) " +
                "ON CONFLICT (message_id) DO NOTHING";

        jdbcTemplate.update(sql, parameters);

        // Update sender and receiver's last interaction time
        sql = "UPDATE users SET last_interaction_time = :last_interaction_time WHERE user_id IN (:user_ids)";
        parameters = new HashMap<>();
        parameters.put("last_interaction_time", LocalDateTime.now());
        parameters.put("user_ids", List.of(message.getSenderUserId(), message.getReceiverUserId()));

        jdbcTemplate.update(sql, parameters);
    }

    public Optional<List<UserConversation>> getConversations() {
        Map<String, Object> parameters = new HashMap<>();

        String sql = "SELECT " +
                "c.conversation_id, " +
                "u.username, " +
                "CASE WHEN CURRENT_TIMESTAMP - u.last_interaction_time < INTERVAL '30 minutes' THEN TRUE ELSE FALSE END AS is_online " +
                "FROM conversations c " +
                "INNER JOIN users u ON c.sender_user_id = u.user_id " +
                "ORDER BY c.created_at ";

        try {
            List<UserConversation> messages = jdbcTemplate.query(sql, parameters, (rs, rowNum) -> UserConversation.builder()
                    .conversationId(UUID.fromString(rs.getString("conversation_id")))
                    .senderName(rs.getString("username"))
                    .isSenderOnline(rs.getBoolean("is_online"))
                    .build());
            return Optional.of(messages);
        } catch (EmptyResultDataAccessException e) {
            return Optional.of(List.of());
        }
    }

    public Optional<Conversation> getMessages(UUID conversationId) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("conversation_id", conversationId);

        String sql = "SELECT m.sender_user_id, m.receiver_user_id, m.message_id, m.body, m.sent_at, s.username as sender, r.username as receiver " +
            "FROM messages m " +
            "INNER JOIN users s ON m.sender_user_id = s.user_id  " +
            "INNER JOIN users r ON m.receiver_user_id = r.user_id " +
            "WHERE conversation_id = :conversation_id::uuid " +
            "ORDER BY m.insert_order";

        try {
            List<Message> messages = jdbcTemplate.query(
                    sql, parameters, (rs, rowNum) -> {
                        Message.MessageBuilder builder = Message.builder()
                                .messageId(UUID.fromString(rs.getString("message_id")))
                                .conversationId(conversationId)
                                .senderUserId(UUID.fromString(rs.getString("sender_user_id")))
                                .receiverUserId(UUID.fromString(rs.getString("receiver_user_id")))
                                .senderName(rs.getString("sender"))
                                .receiverName(rs.getString("receiver"))
                                .body(rs.getString("body"))
                                .sentAt(rs.getTimestamp("sent_at").toLocalDateTime());

                        return builder.build();
                    }
            );

            Conversation conversation = Conversation.builder()
                    .conversationId(conversationId)
                    .messages(messages)
                    .build();

            return Optional.of(conversation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
