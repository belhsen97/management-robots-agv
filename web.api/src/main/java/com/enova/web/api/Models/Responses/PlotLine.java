package com.enova.web.api.Models.Responses;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlotLine {
    private Object value;
    private String text;
}
