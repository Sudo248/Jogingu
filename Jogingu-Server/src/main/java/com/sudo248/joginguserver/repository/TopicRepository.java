package com.sudo248.joginguserver.repository;

import com.google.cloud.firestore.Firestore;
import com.sudo248.joginguserver.repository.entity.Topic;
import org.springframework.stereotype.Repository;

@Repository
public class TopicRepository extends AbstractFirestoreRepository<Topic> {
    protected TopicRepository(Firestore firestore) {
        super(firestore, "topics");
    }
}
