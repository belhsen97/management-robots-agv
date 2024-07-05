package com.enova.web.api.Models.Entitys;


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
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tag {
    @Id
    String id;
    String code;
    String description ;
    String workstationName;
    @JsonIgnore
    Workstation workstation;
    //public void setWorkstation(Workstation w) {if(w==null){return;}this.workstationName = w.getName(); this.workstation = null;   }
    @JsonGetter("workstation")
    public Workstation getWorkstation() {return this.workstation;}
}
