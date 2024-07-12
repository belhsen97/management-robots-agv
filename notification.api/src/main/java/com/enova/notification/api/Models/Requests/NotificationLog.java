package com.enova.notification.api.Models.Requests;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationLog {
    String asctime;
    String name;
    String level;
    String message;
}
