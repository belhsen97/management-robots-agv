package com.enova.web.api.Dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttachmentDto {
     String fileName;
     String downloadURL;
     String fileType;
     long fileSize;
}
