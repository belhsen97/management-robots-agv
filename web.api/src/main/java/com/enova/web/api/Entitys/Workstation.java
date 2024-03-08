package com.enova.web.api.Entitys;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Set;


@Document(collection = "workstation")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Workstation  implements Serializable  {
    @Id
    String id;
    String name ;
    boolean enable ;
    Set<Tag> tags ;

    @JsonIgnore
    Set<Robot> robots ;

    @JsonGetter("robots")
    public  Set<Robot> getRobots() {return this.robots;}
}
//    @Id
//    String id;
//    String name ;
//    TypeStation type ;
//    String location;
//    StatusStation status ;
//    String notice ;
//    boolean enable ;
//    Set<Tag> tags ;
//Maintenance Schedule
//Operating Hours
// Task
//public enum StatusStation {BUSY, AVAILABLE,UNDERMAINTENACE}