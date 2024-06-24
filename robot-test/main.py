
from configurations import GlobalConfiguration
from services import MqttService
from services import RobotService as robot_service
from states import GlobalState as global_state
import argparse
import time
import json

robotService : robot_service.RobotService
clientMqttService : MqttService.MqttService

def on_connection_change(value):
    print(f"connection changed to {value}")
    clientMqttService.publish(global_state.mqttState["publish"]["allData"]+"/property/CONNECTION",json.dumps({"value": value}))

def on_status_change(value):
    print(f"statusRobot changed to {value}")
    clientMqttService.publish(global_state.mqttState["publish"]["allData"]+"/property/STATUS_ROBOT",json.dumps({"value": value}))

def on_mode_change(value):
    print(f"modeRobot changed to {value}")
    clientMqttService.publish(global_state.mqttState["publish"]["allData"]+"/property/MODE_ROBOT",json.dumps({"value": value}))

def on_operation_status_change(value):
    print(f"operationStatus changed to {value}")
    clientMqttService.publish(global_state.mqttState["publish"]["allData"]+"/property/OPERATION_STATUS",json.dumps({"value": value}))

def on_battery_change(value):
    print(f"levelBattery changed to {value}")
    clientMqttService.publish(global_state.mqttState["publish"]["allData"]+"/property/LEVEL_BATTERY",json.dumps({"value": value}))

def on_speed_change(value):
    print(f"speed changed to {value}")
    clientMqttService.publish(global_state.mqttState["publish"]["allData"]+"/property/SPEED",json.dumps({"value": value}))



def callbackSubscribeControlRobot(msg):
      print(f"Subscribe Control Robot `{msg.payload.decode()}` from `{msg.topic}` topic")
      robotService.updatePropertie(msg.topic) #example "topic/robot/control/robot-1/modeRobot/AUTO"


def callbackSubscribeControlAllRobot(msg):
      print(f"Subscribe Control All Robot `{msg.payload.decode()}` from `{msg.topic}` topic")
      robotService.updatePropertie(msg.topic) #example "topic/robot/control/all/modeRobot/AUTO"

def run():
        global_state.robotState["robot"].subscribe("connection", on_connection_change)
        global_state.robotState["robot"].subscribe("statusRobot", on_status_change)
        global_state.robotState["robot"].subscribe("modeRobot", on_mode_change)
        global_state.robotState["robot"].subscribe("operationStatus", on_operation_status_change)
        global_state.robotState["robot"].subscribe("levelBattery", on_battery_change)
        global_state.robotState["robot"].subscribe("speed", on_speed_change)
        global  robotService
        global  clientMqttService
        robotService =  robot_service.RobotService()
        clientMqttService = MqttService.MqttService(global_state.mqttState["broker"],
                                         global_state.mqttState["port"],
                                         global_state.mqttState["client_id"],
                                         global_state.mqttState["username"],
                                         global_state.mqttState["password"])
        clientMqttService.subscribe(global_state.mqttState["subscribe"]["control"],callbackSubscribeControlRobot)
        clientMqttService.subscribe(global_state.mqttState["subscribe"]["controlAll"],callbackSubscribeControlAllRobot)
        while True:
               #clientMqtt.publish()
               robotService.randomData()
               time.sleep(1)

# def run():
#     robot =  robot_service.RobotService()






if __name__ == '__main__':
    #GlobalConfiguration.create_config('config.ini')

    config_data = GlobalConfiguration.read_config('config.ini')
    # Print the retrieved values
    # print("Debug Mode:", config_data['debug_mode'])
    # print("Log Level:", config_data['log_level'])
    # print("MQTT Name:", config_data['broker'])
    # print("MQTT port:", config_data['port'])
    global_state.mqttState["broker"]= config_data['broker']
    global_state.mqttState["port"]= config_data['port'] 


    parser = argparse.ArgumentParser(description='Robot MQTT Client')
    parser.add_argument('-name', '--robot_name', type=str, help='Name of the robot')
    args = parser.parse_args()
    if args.robot_name:
        global_state.mqttState["publish"]["allData"]  = "topic/data/robot/"+ args.robot_name
        global_state.mqttState["subscribe"]["control"] = "topic/control/robot/"+args.robot_name +"/+/+"
        global_state.mqttState["username"]  = args.robot_name
        global_state.mqttState["password"]  = args.robot_name
        global_state.robotState['robot'].name = args.robot_name
        global_state.mqttState["client_id"] =  args.robot_name
        run()
    else:
        print("Please provide the name of the robot using -name or --robot_name argument.")


"""
# Usage


global_state.robotState[ "robot"].subscribe("name", on_name_change)
global_state.robotState[ "robot"].subscribe("statusRobot", on_status_change)
global_state.robotState[ "robot"].subscribe("levelBattery", on_battery_change)

# Access the values
print(f"Current name: {global_state.robotState['robot'].name}")
print(f"Current status: {global_state.robotState['robot'].statusRobot}")
print(f"Current battery level: {global_state.robotState['robot'].levelBattery}")

# Change the values
global_state.robotState[ "robot"].name = "new_robot"
global_state.robotState[ "robot"].statusRobot = "STOPPED"
global_state.robotState[ "robot"].levelBattery = 50"""




