package com.enova.driveless.api.Models.Commons;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConnectionInfo {
    String username;
    long ts;
    int sockport;
    String reason;
    String protocol;
    int proto_ver;
    String proto_name;
    String ipaddress;
    long disconnected_at;
    long connected_at;
    long expiry_interval;
    int keepalive;
    boolean  clean_start;
    String clientid;

 // connection  {"username":"robot-3","ts":1715636591115,"sockport":1883,"protocol":"mqtt","proto_ver":4,"proto_name":"MQTT","keepalive":60,"ipaddress":"172.30.0.1","expiry_interval":0,"connected_at":1715636591115,"clientid":"robot-mqtt-1842","clean_start":true}

 // disconnection  {"username":"robot-3","ts":1715636592031,"sockport":1883,"reason":"tcp_closed","protocol":"mqtt","proto_ver":4,"proto_name":"MQTT","ipaddress":"172.30.0.1","disconnected_at":1715636592031,"connected_at":1715636591115,"clientid":"robot-mqtt-1842"}

}
