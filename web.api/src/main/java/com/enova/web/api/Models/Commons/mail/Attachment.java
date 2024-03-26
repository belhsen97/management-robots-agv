package com.enova.web.api.Models.Commons.mail;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Attachment {
     String id;
     String fileName;
     String fileType;
     long fileSize;
     byte[] data ;

    public String _getNameFile_ ( ){
        if (fileName == null) {return null;}
        String[] words = fileName.split("/"); // split the string into words using the space character as a delimiter
        return ( words.length == 0 ? null : words[words.length-1]) ;
    }
}
