package tn.enova.Models.Dtos;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkstationDto {
    String id;
    String name ;
    boolean enable ;
    String description ;
    List<TagDto> tags ;
}