# src code mqtt client https://www.emqx.com/en/blog/how-to-use-mqtt-in-python
import random
import time
from paho.mqtt import client as mqtt_client
from datetime import datetime, timedelta
import json
import random_robot
import argparse
import gc

broker = 'localhost'
port = 1883
publish_topic = 'topic/robot/data/'# publish_topic     topic/robot/data/  +robot-name
control_topic = 'topic/robot/control/'# control_topic     topic/robot/control/  +robot-name
control_all_robot_topic = 'topic/robot/control/all/+/+'# control_all_robot_topic   topic/robot/control/all

"""
control_topic = 'topic/robot/control/{name robot}/{property}{value} 
property can be ( operationStatus ,  statusRobot ,modeRobot )
"""


client_id = f'robot-mqtt-{random.randint(0, 10000)}'
#"clientid": "enova-robot-1",
username = 'robot'
password = 'robot'
robot_name = 'robot'



class RobotAVG:
    client: mqtt_client
    msg_count = 0
    robot_data = {#"id": "robot_id_" ,
            "name": "robot",
            "connection": "DISCONNECTED",
            "statusRobot": "RUNNING",
            "modeRobot": "AUTO",
            "operationStatus": "PAUSE",
            "createdAt":  datetime.now().isoformat(),  # Format datetime using ISO 8601
            "levelBattery": 0,
            "speed": 0}
    
    def __init__(self, robot_name):
        print("Inside init")
        self.robot_data["name"] = robot_name
        self.client = None
        print ( robot_name) 


    def __enter__(self):
        print("Inside enter")
        self.client = self.connect_mqtt()
        self.client.loop_start()
        self.robot_data["connection"] = "CONNECTED" 
        msg = json.dumps(self.robot_data)
        self.publish(msg)
        return self
        
    def __exit__(self, exc_type, exc_value, traceback):
        print("\nInside __exit__")
        self.robot_data["connection"] = "DISCONNECTED" 
        msg = json.dumps(self.robot_data)
        self.publish(msg)
        # print("\nExecution type:", exc_type)
        # print("\nExecution value:", exc_value)
        # print("\nTraceback:", traceback)
 
    def __del__(self):
        print("__del__")
        self.client.loop_stop()
        self.disconnect()

    def connect_mqtt(self):
        def on_connect(client, userdata, flags, rc):
            if rc == 0:
                print("Connected to MQTT Broker!")
            else:
                print("Failed to connect, return code %d\n", rc)

        client = mqtt_client.Client(mqtt_client.CallbackAPIVersion.VERSION1, client_id) 
        # client = mqtt_client.Client(client_id) Paho MQTT 'Unsupported callback API version' error
        # client.tls_set(ca_certs='./server-ca.crt')
        client.username_pw_set(username, password)
        client.on_connect = on_connect
        client.connect(broker, port)
        return client
        
        
    def is_connected(self):
        return self.client.is_connected()

    def unsubscribe(self, *topics):
        self.client.unsubscribe(*topics)    

    def disconnect(self):
        if not self.is_connected():
            self.client.disconnect()    
    
    
    def subscribe(self, topic):#topic/robot/control/robot-1/ModeRobot/MANUAL
        def on_message(client, userdata, msg):
            print(f"Received `{msg.payload.decode()}` from `{msg.topic}` topic")
            print(f"Received data from `{msg.topic}` topic:")
            parts = msg.topic.split('/')
            lengthParts = len(parts)
            if lengthParts != 6:
               return
            print(f"attribut  `{parts[lengthParts-2]}` value: `{parts[lengthParts-1]}`")
            if not( (parts[lengthParts-2] == "ModeRobot") or ( parts[lengthParts-2] == "OperationStatus")):
               return
            if (parts[lengthParts-2] == "ModeRobot") and ((parts[lengthParts-1] == "MANUAL")or(parts[lengthParts-1] == "AUTO")) :
                 self.robot_data["modeRobot"] = parts[lengthParts-1] 
            if (parts[lengthParts-2] == "OperationStatus") and ((parts[lengthParts-1] == "NORMAL")or(parts[lengthParts-1] == "EMS")or(parts[lengthParts-1] == "PAUSE")) :
                 self.robot_data["operationStatus"] = parts[lengthParts-1]  
            msg = json.dumps(self.robot_data)
            self.publish(msg)    
                   
        self.client.subscribe(topic)
        self.client.on_message = on_message

    def subscribeAll(self,topic):#topic/robot/control/all/ModeRobot/MANUAL
        def on_message(client, userdata, msg):
            print(f"Received `{msg.payload.decode()}` from `{msg.topic}` topic")
            print(f"Received data from `{msg.topic}` topic:")
            parts = msg.topic.split('/')
            lengthParts = len(parts)
            if lengthParts != 6:
               return
            print(f"attribut  `{parts[lengthParts-2]}` value: `{parts[lengthParts-1]}`")
            if not( (parts[lengthParts-2] == "ModeRobot") or ( parts[lengthParts-2] == "OperationStatus")):
               return
            if (parts[lengthParts-2] == "ModeRobot") and ((parts[lengthParts-1] == "MANUAL")or(parts[lengthParts-1] == "AUTO")) :
                 self.robot_data["modeRobot"] = parts[lengthParts-1] 
            if (parts[lengthParts-2] == "OperationStatus") and ((parts[lengthParts-1] == "NORMAL")or(parts[lengthParts-1] == "EMS")or(parts[lengthParts-1] == "PAUSE")) :
                 self.robot_data["operationStatus"] = parts[lengthParts-1]
            if (parts[lengthParts-2] == "StatusRobot") and ((parts[lengthParts-1] == "INACTIVE")or(parts[lengthParts-1] == "RUNNING")or(parts[lengthParts-1] == "WAITING")) :
                 self.robot_data["statusRobot"] = parts[lengthParts-1]       
            msg = json.dumps(self.robot_data)
            self.publish(msg)

        self.client.subscribe(topic)
        self.client.on_message = on_message

    def publish(self,msg):
           result = self.client.publish(publish_topic, msg)
           status = result[0]
           if status == 0:
              print(f"Sent data for {self.robot_data['name'] } to topic {publish_topic}")
           else:
              print(f"Failed to send data for {self.robot_data['name'] } to topic {publish_topic}")
              self.msg_count += 1

    def publish_loop_data(self):
        while True:
           time.sleep(1)
           random_robot.update_connection_status()
           #random_robot.update_robot_status()
           random_robot.update_battery_level()
           random_robot.update_speed_value()
           #random_robot.update_operation_status()
           #random_robot.update_robot_mode()
           #self.robot_data["name"] = self.robot_name
           #self.robot_data["connection"] = "CONNECTED"  #random_robot.connection_status
           #self.robot_data["statusRobot"] = random_robot.status_robot
           #self.robot_data["modeRobot"] = random_robot.mode_robot
           #self.robot_data["operationStatus"] = random_robot.operation_status
           self.robot_data["createdAt"] =  datetime.now().isoformat()
           self.robot_data["levelBattery"] =  random_robot.battery_level 
           self.robot_data["speed"] =  random_robot.speed_value
               
               
           msg = json.dumps(self.robot_data)
           self.publish(msg)


# a = RobotAVG("robot_name")
# del a 

# with RobotAVG("robot_name") as client:
    # print('with statement block')  
    
def run():
  with RobotAVG(robot_name) as client:
        client.subscribe(control_topic)
        client.subscribeAll(control_all_robot_topic)
        client.publish_loop_data()


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Robot MQTT Client')
    parser.add_argument('-name', '--robot_name', type=str, help='Name of the robot')
    args = parser.parse_args()
    if args.robot_name:
        publish_topic += args.robot_name
        control_topic += args.robot_name +"/+/+"
        print(control_topic)
        username = args.robot_name
        password = args.robot_name
        robot_name = args.robot_name
        run()
    else:
        print("Please provide the name of the robot using -name or --robot_name argument.")
# if __name__ == '__main__':
    # run()









"""

{
  "name": "robot-1",
  "connection": "DISCONNECTED",
  "statusRobot": "RUNNING",
  "modeRobot": "MANUAL",
  "operationStatus": "PAUSE",
  "levelBattery": 100,
  "speed": 2.0
}


robot_data = {#"id": "robot_id_" + str(msg_count),
            "name": robot_name,
            "connection": "DISCONNECTED",
            "statusRobot": "RUNNING",
            "modeRobot": "AUTO",
            "operationStatus": "PAUSE",
            "createdAt":  datetime.now().isoformat(),  # Format datetime using ISO 8601
            "levelBattery": 0,
            "speed": 0}


def connect_mqtt():
    def on_connect(client, userdata, flags, rc):
        if rc == 0:
            print("Connected to MQTT Broker!")
        else:
            print("Failed to connect, return code %d\n", rc)

    client = mqtt_client.Client(mqtt_client.CallbackAPIVersion.VERSION1, client_id) 
    # client = mqtt_client.Client(client_id) Paho MQTT 'Unsupported callback API version' error
    # client.tls_set(ca_certs='./server-ca.crt')
    client.username_pw_set(username, password)
    client.on_connect = on_connect
    client.connect(broker, port)
    return client







 


def subscribe(client: mqtt_client):
    def on_message(client, userdata, msg):
        #print(f"Received `{msg.payload.decode()}` from `{msg.topic}` topic")
        payload = msg.payload.decode()
        try:
            data = json.loads(payload)
            print(f"Received data from `{msg.topic}` topic:")
            print(json.dumps(data, indent=2))  # is used to specify the number of spaces 
            robot_data["modeRobot"] = data.get("modeRobot")
            robot_data["statusRobot"] = data.get("statusRobot")
            robot_data["operationStatus"]  = data.get("operationStatus")
            # Process the received data further as needed
        except json.JSONDecodeError:
            print("Received payload is not in JSON format:", payload)

    client.subscribe(control_topic)
    client.on_message = on_message

def subscribeAll(client: mqtt_client):
    def on_message(client, userdata, msg):
        #print(f"Received `{msg.payload.decode()}` from `{msg.topic}` topic")
        payload = msg.payload.decode()
        try:
            data = json.loads(payload)
            print(f"Received data from `{msg.topic}` topic:")
            print(json.dumps(data, indent=2))  # is used to specify the number of spaces 
            robot_data["modeRobot"] = data.get("modeRobot")
            robot_data["statusRobot"] = data.get("statusRobot")
            robot_data["operationStatus"]  = data.get("operationStatus")
            # Process the received data further as needed
        except json.JSONDecodeError:
            print("Received payload is not in JSON format:", payload)

    client.subscribe(control_all_robot_topic)
    client.on_message = on_message
    
    
def unsubscribe(client: mqtt_client, *topics):
    client.unsubscribe(*topics)    
def disconnect(client: mqtt_client):
    client.disconnect()        
def publish(client):
    msg_count = 0
    while True:
        time.sleep(1)
        random_robot.update_connection_status()
        #random_robot.update_robot_status()
        random_robot.update_battery_level()
        random_robot.update_speed_value()
        #random_robot.update_operation_status()
        #random_robot.update_robot_mode()
        robot_data["name"] = robot_name
        robot_data["connection"] = random_robot.connection_status
        #robot_data["statusRobot"] = random_robot.status_robot
        #robot_data["modeRobot"] = random_robot.mode_robot
        #robot_data["operationStatus"] = random_robot.operation_status
        robot_data["createdAt"] =  datetime.now().isoformat()
        robot_data["levelBattery"] =  random_robot.battery_level 
        robot_data["speed"] =  random_robot.speed_value
               
               
        msg = json.dumps(robot_data)
        result = client.publish(publish_topic, msg)
        status = result[0]
        if status == 0:
            print(f"Sent data for {robot_name} to topic {publish_topic}")
        else:
            print(f"Failed to send data for {robot_name} to topic {publish_topic}")
        msg_count += 1
        if msg_count % 1000 == 0:  # For example, trigger GC every 1000 iterations
            gc.collect()

def run():
    client = connect_mqtt()
    subscribe(client)
    subscribeAll(client)
    client.loop_start()
    publish(client)


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Robot MQTT Client')
    parser.add_argument('-name', '--robot_name', type=str, help='Name of the robot')
    args = parser.parse_args()
    if args.robot_name:
        publish_topic += args.robot_name
        control_topic += args.robot_name
        username = args.robot_name
        password = args.robot_name
        robot_name = args.robot_name
        
        run()
    else:
        print("Please provide the name of the robot using -name or --robot_name argument.")
# if __name__ == '__main__':
    # run()
"""




