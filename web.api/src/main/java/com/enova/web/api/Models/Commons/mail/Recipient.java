package com.enova.web.api.Models.Commons.mail;

import com.enova.web.api.Enums.RecipientType;
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
