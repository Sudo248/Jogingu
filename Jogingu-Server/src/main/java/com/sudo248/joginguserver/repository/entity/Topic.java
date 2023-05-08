package com.sudo248.joginguserver.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Topic {
    private String topicName;
    private Date lastPublish;

    public Topic(String topicName) {
        this.topicName = topicName;
        lastPublish = new Date();
    }
}
