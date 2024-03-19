package com.enova.web.api.Dtos;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TraceDto {
    @Id
    String id;
    String username;
    Date timestamp;
    String description;
    String className;
    String methodName;
    UserDto user;
}