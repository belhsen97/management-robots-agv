package com.enova.collector.api.Models.Commons;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subscribe {
    String topic;
    int qos = 0;//https://www.hivemq.com/blog/mqtt-essentials-part-6-mqtt-quality-of-service-levels/
}
