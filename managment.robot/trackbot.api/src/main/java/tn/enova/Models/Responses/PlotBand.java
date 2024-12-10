package tn.enova.Models.Responses;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class PlotBand {
    private Long from;
    private Long to;
    private String text;
}