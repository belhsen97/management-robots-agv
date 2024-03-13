package com.enova.web.api.Entitys;


import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tag {
    @Id
    String id;
    String code;
    String description ;
    String workstationName;


    @JsonIgnore
    Workstation workstation;
    @JsonGetter("workstation")
    public Workstation getWorkstation() {return this.workstation;}
}
