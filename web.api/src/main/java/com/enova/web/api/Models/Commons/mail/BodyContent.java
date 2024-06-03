package com.enova.web.api.Models.Commons.mail;

import com.enova.web.api.Enums.TypeBody;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Map;
import java.util.HashMap;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BodyContent {
    Long id;
    TypeBody type; //  text/plain or  text/html
    String content;
    Map<String, BodyContent> embeddeds = new HashMap<>();
}