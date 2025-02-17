package tn.enova.Models.Commons.mail;

import tn.enova.Enums.RecipientType;
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
