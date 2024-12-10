package tn.enova.Models.Commons;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Method;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExceptionHandler {
    Class<? extends Throwable> exceptionType;
    Method method;
    Object instance;
}