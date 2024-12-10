package tn.enova.Models.Entitys;

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
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Workstation implements Serializable {
    @Id
    String id;
    String name;
    boolean enable;
    String description;
    @JsonIgnore
    Set<Tag> tags;

    @JsonGetter("tags")
    public Set<Tag> getTags() {
        return this.tags;
    }
}