`GET /messages/conversations` returns all the conversations in the system

e.g. 

```
{
    "conversations": [
        {
            "conversationId": "e8a7105e-f03f-4ab5-9ca9-15623bedf119",
            "senderName": "jimquitevis",
            "senderOnline": true
        },
        {
            "conversationId": "6cd3e411-7192-46ff-a5e9-481ccbc87f94",
            "senderName": "jacobliam",
            "senderOnline": true
        },
        {
            "conversationId": "eb7025d7-a958-4bf4-a8af-8a4e75c6c2fc",
            "senderName": "jimquitevis",
            "senderOnline": true
        }
        ]
    }
}
```
* `senderOnline` is set to true if the difference between `now` and time of the last activity of the user (stored in the `users` table) is less than 30 minutes. 
* `senderName` is the username of the user that created the conversation

`GET /messages/conversations/{conversation id}` returns the messages for a given conversation
e.g.
```
{
    "conversationId": "e8a7105e-f03f-4ab5-9ca9-15623bedf119",
    "messages": [
        {
            "messageId": "0ebf9f87-423f-4f11-ae3d-434aba14ef57",
            "conversationId": "e8a7105e-f03f-4ab5-9ca9-15623bedf119",
            "senderUserId": "aeafdff1-f5a2-4da9-9e7a-b18e85eb3b4b",
            "receiverUserId": "30dde09b-05b7-47a1-ad4d-ff29d0ed4ef8",
            "senderName": "jimquitevis",
            "receiverName": "jacobliam",
            "body": "Hi there!",
            "sentAt": "2024-01-30T18:00:43.238997"
        },
        {
            "messageId": "9230100b-f2be-40c6-b421-a86b8ff3a1e9",
            "conversationId": "e8a7105e-f03f-4ab5-9ca9-15623bedf119",
            "senderUserId": "30dde09b-05b7-47a1-ad4d-ff29d0ed4ef8",
            "receiverUserId": "aeafdff1-f5a2-4da9-9e7a-b18e85eb3b4b",
            "senderName": "jacobliam",
            "receiverName": "jimquitevis",
            "body": "Oh hey, how are you?",
            "sentAt": "2024-01-30T18:00:43.243826"
        },
        {
            "messageId": "09670da4-e358-444d-9868-8fa071711782",
            "conversationId": "e8a7105e-f03f-4ab5-9ca9-15623bedf119",
            "senderUserId": "30dde09b-05b7-47a1-ad4d-ff29d0ed4ef8",
            "receiverUserId": "aeafdff1-f5a2-4da9-9e7a-b18e85eb3b4b",
            "senderName": "jacobliam",
            "receiverName": "jimquitevis",
            "body": "Hey!! Can you answer?",
            "sentAt": "2024-01-30T18:00:43.246361"
        }
    ]
}
```
* A column called `insert_order` that's auto incrementing is used to sort the message order cause timestamp can contain the same value. It is atomically incremented so it is thread safe.

I wrote a simple integration test called `MessageRepositoryIntegrationTest` which tests the behavior of both the functions.
There's a flyway script that provisions the postgres database. Run `./gradlew flywayMigrate` to apply the migration. 
