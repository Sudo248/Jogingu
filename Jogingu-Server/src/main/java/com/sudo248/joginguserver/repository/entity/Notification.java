package com.sudo248.joginguserver.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private String image;
    private String title;
    private String body;

    public com.google.firebase.messaging.Notification toFirebaseNotification() {
        com.google.firebase.messaging.Notification.Builder builder = com.google.firebase.messaging.Notification.builder();
        if (image != null) {
            builder.setImage(image);
        }
        return builder
                .setTitle(title)
                .setBody(body)
                .build();
    }
}
