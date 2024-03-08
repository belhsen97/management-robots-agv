package com.enova.web.api.Entitys;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Attachment implements Serializable {
    String id;
    String fileName;
    String fileType;
    long fileSize;
    byte[] data;
}
