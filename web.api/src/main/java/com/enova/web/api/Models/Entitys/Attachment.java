package com.enova.web.api.Models.Entitys;

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
    @Override
    public String toString() {
        return "Attachment{" +
                "fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize=" + fileSize +
                '}';
    }
}
