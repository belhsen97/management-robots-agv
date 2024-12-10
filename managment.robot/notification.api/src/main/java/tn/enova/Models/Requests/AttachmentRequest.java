package tn.enova.Models.Requests;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttachmentRequest {
     String fileName;
     String downloadURL;
     String fileType;
     long fileSize;
}
