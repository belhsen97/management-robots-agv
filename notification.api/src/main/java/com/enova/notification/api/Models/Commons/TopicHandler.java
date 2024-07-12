package com.enova.notification.api.Models.Commons;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Method;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopicHandler {
    String topic;
    Method method;
    Object instance;
}