package com.enova.web.api.Dtos.mail;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Msg {
    Long id;
    String subject;
    String email;
    String text;
    List<BodyContent> bodyContents = new ArrayList<BodyContent>();
    Date  sentDate;
    Date  receivedDate;
    List<Attachment>  attachements = new ArrayList<Attachment>();
}
/*
* up to JDK7:
@AllArgsConstructor(onConstructor=@__({@AnnotationsGoHere}))
from JDK8:
@AllArgsConstructor(onConstructor_={@AnnotationsGohere}) // note the underscore after onConstructor.*/