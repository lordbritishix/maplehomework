package com.lordbritishix.maplehomework.controller;

import com.lordbritishix.maplehomework.dto.GetConversationsResponse;
import com.lordbritishix.maplehomework.dto.GetMessagesResponse;
import com.lordbritishix.maplehomework.service.MessagesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.function.Supplier;

@RestController
@Slf4j
@RequestMapping("/messages")
public class MessagesController {
    private final MessagesService messagesService;

    @Autowired
    public MessagesController(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @GetMapping(value = "/conversations/{conversationId}")
    @ResponseBody
    public ResponseEntity<GetMessagesResponse> getMessages(@PathVariable String conversationId) throws Throwable {
        return messagesService.getMessages(UUID.fromString(conversationId)).map(ResponseEntity::ok)
                .orElseThrow((Supplier<Throwable>) () -> new IllegalArgumentException("Unable to find messages for conversation id " + conversationId));
    }

    @GetMapping(value = "/conversations")
    @ResponseBody
    public ResponseEntity<GetConversationsResponse> getConversations() throws Throwable {
        // TODO: userId would probably come from the request header but for simplicity, just pass it in
        return messagesService.getConversations().map(ResponseEntity::ok)
                .orElseThrow((Supplier<Throwable>) () -> new IllegalArgumentException("Unable to find any conversations "));
    }
}
