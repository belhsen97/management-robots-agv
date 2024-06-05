package com.enova.web.api.Models.Commons.mail;

import com.enova.web.api.Enums.RecipientType;
import com.enova.web.api.Enums.StatusMsg;
import com.enova.web.api.Models.Entitys.Attachment;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Msg {
    String id;
    String subject;
    List<Recipient> recipients = new ArrayList<>();
    List<BodyContent> bodyContents = new ArrayList<BodyContent>();
    Date timestamp;
    StatusMsg status;
    List<Attachment>  attachements = new ArrayList<Attachment>();

    public List<Recipient> getRecipientsByType(RecipientType type ){
        return this.recipients.stream()
                .filter(r -> r.getType().equals(type))
                .collect(Collectors.toList());
    }
    public void addAttachments (MultipartFile[] files) throws IOException {
        if ( this.attachements == null ){this.attachements = new ArrayList<Attachment>();}
        if ( files == null ) {return;}
        for (MultipartFile  file : files ){
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Attachment attachment = Attachment.builder()
                    .fileName(fileName)
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .data(file.getBytes())
                    .build();
            this.attachements.add(attachment);
        }
    }
}
/*
* up to JDK7:
@AllArgsConstructor(onConstructor=@__({@AnnotationsGoHere}))
from JDK8:
@AllArgsConstructor(onConstructor_={@AnnotationsGohere}) // note the underscore after onConstructor.*/