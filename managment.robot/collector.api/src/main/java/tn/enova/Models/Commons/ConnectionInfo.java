package tn.enova.Models.Commons;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConnectionInfo {
    String username;
    @JsonProperty("clientid")
    String clientId;
    @JsonProperty("ts")
    private long timestamp;
    @JsonProperty("sockport")
    private int socketPort;
    String reason;
    String protocol;
    @JsonProperty("proto_ver")
    int protocolVersion;
    @JsonProperty("proto_name")
    String protocolName;
    @JsonProperty("ipaddress")
    String ipAddress;
    @JsonProperty("disconnected_at")
    long disconnectedAt;
    @JsonProperty("connected_at")
    long connectedAt;
    @JsonProperty("expiry_interval")
    long expiryInterval;
    @JsonProperty("keepalive")
    int keepAlive;
    @JsonProperty("clean_start")
    boolean  cleanStart;

    Map<String, Object> additionalProperties = new HashMap<>();
    @JsonAnySetter
    public void handleUnknownField(String key, Object value) {
        additionalProperties.put(key, value);
    }
 // connection  {"username":"robot-3","ts":1715636591115,"sockport":1883,"protocol":"mqtt","proto_ver":4,"proto_name":"MQTT","keepalive":60,"ipaddress":"172.30.0.1","expiry_interval":0,"connected_at":1715636591115,"clientid":"robot-mqtt-1842","clean_start":true}
 // disconnection  {"username":"robot-3","ts":1715636592031,"sockport":1883,"reason":"tcp_closed","protocol":"mqtt","proto_ver":4,"proto_name":"MQTT","ipaddress":"172.30.0.1","disconnected_at":1715636592031,"connected_at":1715636591115,"clientid":"robot-mqtt-1842"}
}
