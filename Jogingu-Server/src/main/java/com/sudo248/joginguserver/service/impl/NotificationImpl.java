package com.sudo248.joginguserver.service.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.sudo248.joginguserver.repository.TopicRepository;
import com.sudo248.joginguserver.repository.UserRepository;
import com.sudo248.joginguserver.repository.entity.Notification;
import com.sudo248.joginguserver.repository.entity.Topic;
import com.sudo248.joginguserver.repository.entity.User;
import com.sudo248.joginguserver.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationImpl implements NotificationService {

    private final UserRepository userRepository;

    private final TopicRepository topicRepository;

    private final FirebaseMessaging firebaseMessaging;

    public NotificationImpl(UserRepository userRepository, TopicRepository topicRepository, FirebaseMessaging firebaseMessaging) {
        this.userRepository = userRepository;
        this.topicRepository = topicRepository;
        this.firebaseMessaging = firebaseMessaging;
    }

    @Override
    public String sendNotificationToTopic(String userId, String topic, Notification notification) {
        Message message = Message.builder().setTopic(topic).setNotification(notification.toFirebaseNotification()).build();
        firebaseMessaging.sendAsync(message);
        topicRepository.save(new Topic(topic));
        return "Success";
    }

    @Override
    public String sendNotification(String userId, String otherId, Notification notification) {
        Optional<User> user = userRepository.get(otherId);
        return user.map(value -> sendNotificationToToken(value.getToken(), notification)).orElse("Not found user");
    }

    @Override
    public boolean saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean saveToken(String userId, String token) {
        Optional<User> user = userRepository.get(userId);
        if (user.isPresent()) {
            User userValue = user.get();
            userValue.setToken(token);
            return userRepository.save(userValue);
        }
        return false;
    }

    @Override
    public String sendNotificationToToken(String token, Notification notification) {
        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification.toFirebaseNotification())
                .build();

        firebaseMessaging.sendAsync(message);
        return "Success";
    }

    @Override
    public String sendNotificationPayment(String userId, Notification notification) {
        Optional<User> user = userRepository.get(userId);
        if (user.isPresent() && user.get().getToken() != null) {
            return sendNotificationToToken(user.get().getToken(), notification);
        }
        return "Not found user";
    }
}
