package tn.enova.Models.Commons.mail;

import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.enova.Enums.TypeBody;

import java.util.HashMap;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BodyContent {
    TypeBody type; //  text/plain or  text/html
    String content;
    Map<String, BodyContent> embeddeds = new HashMap<>();
}