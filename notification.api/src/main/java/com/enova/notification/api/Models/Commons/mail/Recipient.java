package com.enova.notification.api.Models.Commons.mail;

import com.enova.notification.api.Enums.RecipientType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Recipient {
    String address;
    RecipientType type;
}
