package com.enova.collector.api.Listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@Retention(RetentionPolicy.RUNTIME)
//@Target(ElementType.METHOD)
//public @interface ListenerTopic {
//    String topic();
//}
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ListenerTopic {
    int qos() default 0;
    String topic();
}
