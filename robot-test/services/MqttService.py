# src code mqtt client https://www.emqx.com/en/blog/how-to-use-mqtt-in-python
from paho.mqtt import client as mqtt_client


class MqttService:
    client: mqtt_client
    broker = 'localhost'
    port = 1883
    client_id= "robot"
    username = 'robot'
    password = 'robot'
    msg_count = 0
    subscriptions = [] 
    def __init__(self,broker,port,client_id,username,password):
        print("\nMQTTService :  __init__")
        self.client = None
        self.broker = broker
        self.port = port
        self.client_id= client_id
        self.username = username
        self.password = password
        self.subscriptions = [] 
        self.client = self.connect_mqtt()
        self.client.loop_start()

    def __enter__(self):
        print("\nMQTTService : __enter__")
        return self
        
    def __exit__(self, exc_type, exc_value, traceback):
        print("\nMQTTService : __exit__")
 
    def __del__(self):
        print("__del__")
        self.client.loop_stop()
        self.disconnect()

    def connect_mqtt(self) -> mqtt_client:
        def on_connect(client, userdata, flags, rc):
            if rc == 0:
                print("Connected to MQTT Broker!")
            else:
                print("Failed to connect, return code %d\n", rc)

        client = mqtt_client.Client(mqtt_client.CallbackAPIVersion.VERSION1, self.client_id) 
        # client = mqtt_client.Client(client_id) Paho MQTT 'Unsupported callback API version' error
        # client.tls_set(ca_certs='./server-ca.crt')
        client.username_pw_set(self.username, self.password)
        client.on_connect = on_connect
        client.connect(self.broker, self.port)
        return client
        
        
    def is_connected(self):
        return self.client.is_connected()

    def unsubscribe(self, *topics):
        self.client.unsubscribe(*topics)    

    def disconnect(self):
        if not self.is_connected():
            self.client.disconnect()    
    
    
    def subscribe(self, topic , callback=None ):#topic/robot/control/robot-1/ModeRobot/MANUAL
        def on_message(client, userdata, msg):
            # print(f"Received `{msg.payload.decode()}` from `{msg.topic}` topic")
            # print(f"Received data from `{msg.topic}` topic:")
            # if callback != None:
            #    callback(msg)
            for (sub_topic, sub_callback) in self.subscriptions:
                if ((sub_topic == msg.topic or self.match( msg.topic,sub_topic)) and sub_callback):
                    sub_callback(msg)
                   
        self.subscriptions.append((topic, callback))
        self.client.subscribe(topic)
        self.client.on_message = on_message


    def publish(self,topic,msg):
           result = self.client.publish(topic, msg)
           status = result[0]
           if status == 0:
              print(f"Send data  to topic {topic}")
           else:
              print(f"Failed to send data to topic {topic}")
              self.msg_count += 1

    def match(self,topic, pattern):
        topic_parts = topic.split('/')
        pattern_parts = pattern.split('/')

        topic_length = len(topic_parts)
        pattern_length = len(pattern_parts)

        if pattern.endswith('/#'):
           pattern_length -= 1
           if topic_length < pattern_length:
               return False
        else:
           if topic_length != pattern_length:
              return False

        for i in range(pattern_length):
            if not self.match_parts(topic_parts[i], pattern_parts[i]):
               return False
        return True

    def match_parts(self,topic_part, pattern_part):
        if pattern_part == '#':
           return True
        elif pattern_part == '+':
              return '/' not in topic_part
        else:
            return topic_part == pattern_part