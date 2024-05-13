package com.enova.driveless.api.Models.Commons;

import lombok.*;
import lombok.experimental.FieldDefaults;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Publish extends Subscribe {
    @Builder(builderMethodName = "childBuilder")
    public Publish(String topic, int qos,byte[]  payload, boolean retained) {
        super(topic ,qos);
        this.payload = payload;
        this.retained = retained;
    }

    byte[] payload;
    boolean retained = false;
}