export interface StatusClientMQTT {
    clean_start: boolean; // clean_start: true;
    clientid: String; // clientid: "robot-1";
    connected_at: Number; // connected_at: 1717337538741;
    disconnected_at: Number;//disconnected_at: 1717337551314
    expiry_interval: Number; // expiry_interval: 0;
    ipaddress: String; // ipaddress: "192.168.0.1";
    keepalive: Number; // keepalive: 60;
    proto_name: String; // proto_name: "MQTT";
    proto_ver: Number; // proto_ver: 4;
    protocol: String; //protocol: "mqtt"
    sockport : number;//sockport: 1883
    ts: number;  //ts: 1717337538741
    username : String;//username: "robot-1"
    reason: String;//reason: "tcp_closed"
}