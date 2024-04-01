import random
import time
from paho.mqtt import client as mqtt_client
from datetime import datetime, timedelta
import json
import random_robot
import argparse

broker = 'localhost'
port = 1883
topic = 'topic/robot/1'
client_id = f'python-mqtt-{random.randint(0, 1000)}'
username = 'test2'
password = 'test2'

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


def publish(client):
    msg_count = 0
    while True:
        time.sleep(1)
        msg = f"messages: {msg_count}"
        result = client.publish(topic, msg)
        # result: [0, 1]
        status = result[0]
        if status == 0:
            print(f"Send `{msg}` to topic `{topic}`")
        else:
            print(f"Failed to send message to topic {topic}")
        msg_count += 1




 





        
def publishRobot(client, robot_name):
    msg_count = 0
    while True:
        time.sleep(1)
        random_robot.update_connection_status()
        random_robot.update_robot_status()
        random_robot.update_battery_level()
        random_robot.update_speed_value()
        random_robot.update_operation_status()
        random_robot.update_robot_mode()
        robot_data = {
            #"id": "robot_id_" + str(msg_count),
            "name": robot_name ,
            "connection": random_robot.connection_status,
            "statusRobot": random_robot.status_robot,
            "modeRobot": random_robot.mode_robot,
            "operationStatus": random_robot.operation_status,
            "createdAt":  datetime.now().isoformat(),  # Format datetime using ISO 8601
            "levelBattery": random_robot.battery_level,
            "speed": random_robot.speed_value
        }
        msg = json.dumps(robot_data)
        result = client.publish(topic, msg)
        status = result[0]
        if status == 0:
            print(f"Sent data for {robot_name} to topic {topic}")
        else:
            print(f"Failed to send data for {robot_name} to topic {topic}")
        msg_count += 1

def run(robot_name):
    client = connect_mqtt()
    client.loop_start()
    publishRobot(client, robot_name)


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Robot MQTT Client')
    parser.add_argument('-name', '--robot_name', type=str, help='Name of the robot')
    args = parser.parse_args()
    
    if args.robot_name:
        run(args.robot_name)
    else:
        print("Please provide the name of the robot using -name or --robot_name argument.")
# if __name__ == '__main__':
    # run()
