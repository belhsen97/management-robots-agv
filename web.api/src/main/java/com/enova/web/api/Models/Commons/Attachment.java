package com.enova.web.api.Models.Commons;

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
    public String _getNameFile_ ( ){
        if (fileName == null) {return null;}
        String[] words = fileName.split("/"); // split the string into words using the space character as a delimiter
        return ( words.length == 0 ? null : words[words.length-1]) ;
    }
}
