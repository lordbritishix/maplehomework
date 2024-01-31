CREATE TABLE IF NOT EXISTS users
(
    user_id UUID NOT NULL CONSTRAINT pk_user_id PRIMARY KEY,
    username VARCHAR NOT NULL,
    last_interaction_time TIMESTAMP
);

CREATE TABLE IF NOT EXISTS conversations
(
    conversation_id UUID NOT NULL CONSTRAINT pk_conversation_id PRIMARY KEY,
    sender_user_id UUID NOT NULL, -- I understood this to be the creator of the conversation
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    FOREIGN KEY (sender_user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS messages (
    insert_order SERIAL NOT NULL, -- so we can sort messages properly - cause timestamp is not unique and can be duplicated
    message_id UUID NOT NULL CONSTRAINT pk_message_id PRIMARY KEY,
    conversation_id UUID NOT NULL,
    sender_user_id UUID NOT NULL,
    receiver_user_id UUID NOT NULL,
    body TEXT NOT NULL,
    sent_at TIMESTAMP NOT NULL DEFAULT now(),
    FOREIGN KEY (sender_user_id) REFERENCES users (user_id),
    FOREIGN KEY (receiver_user_id) REFERENCES users (user_id),
    FOREIGN KEY (conversation_id) REFERENCES conversations (conversation_id)
);

