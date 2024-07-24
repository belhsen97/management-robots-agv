from configurations import GlobalConfiguration
from services import MqttService as mqtt_service
from services import RobotService as robot_service
from states import GlobalState as state
from effects import RobotEffect as robot_effect
from effects import MqttEffect as mqtt_effect
from effects import NotificationEffect as notification_effect
from datetime import datetime
import argparse
import time
import sys
from enums import NotificationEnum as notification_enum


# robotService : robot_service.RobotService
# clientMqttService : mqtt_service.MqttService

def run():
        state.notification.subscribe("notification", notification_effect.on_notification_change)
        state.robotState["robot"].subscribe("connection", robot_effect.on_connection_change)
        state.robotState["robot"].subscribe("statusRobot", robot_effect.on_status_change)
        state.robotState["robot"].subscribe("modeRobot", robot_effect.on_mode_change)
        state.robotState["robot"].subscribe("operationStatus", robot_effect.on_operation_status_change)
        state.robotState["robot"].subscribe("levelBattery", robot_effect.on_battery_change)
        state.robotState["robot"].subscribe("speed", robot_effect.on_speed_change)
        state.robotState["robot"].subscribe("codeTag", robot_effect.on_code_tag_change)
        state.robotState["robot"].subscribe("distance", robot_effect.on_distance_change)
        state.robotState["service"] = robot_service.RobotService()
        state.mqttState["service"] = mqtt_service.MqttService(state.mqttState["broker"],
                                         state.mqttState["port"],
                                         state.mqttState["client_id"],
                                         state.mqttState["username"],
                                         state.mqttState["password"])
        state.mqttState["service"].subscribe(state.mqttState["subscribe"]["control"],mqtt_effect.onSubscribeControlRobot)
        state.mqttState["service"].subscribe(state.mqttState["subscribe"]["controlAll"],mqtt_effect.onSubscribeControlAllRobot)
        state.mqttState["service"].subscribe(state.mqttState["subscribe"]["lastUpdate"],mqtt_effect.onSubscribeInitDataRobot)
        state.robotState["service"].getRobotCodes()
        state.robotState["service"].getRobot( state.robotState['robot'].name )

        # while  not state.mqttState["service"].is_connected() :
        #        print (state.mqttState["publish"]["lastUpdate"]  )
        #        state.mqttState["service"].publish( state.mqttState["publish"]["lastUpdate"] ,"vide")
        #        time.sleep(1)
        count = 0
        while True:
        #    if state.robotState['robot'].statusRobot  == "INACTIVE" :
        #       sys.exit("Stopping the script")
        #    state.robotState['robot'].createdAt =  datetime.now().isoformat()
             count+=1
             state.notification("name1", notification_enum.LevelType.INFO, "message"+str(count), "asctime1")
             time.sleep(1)
       








if __name__ == '__main__':
    #GlobalConfiguration.create_config('config.ini')

    config_data = GlobalConfiguration.read_config('config.ini')
    # Print the retrieved values
    # print("Debug Mode:", config_data['debug_mode'])
    # print("Log Level:", config_data['log_level'])
    print("MQTT Name:", config_data['mqtt']['host'])
    print("MQTT port:", config_data['mqtt']['port'])
    print("HTTP Name:", config_data['http']['host'])
    print("HTTP port:", config_data['http']['port'])
    state.mqttState["broker"]= config_data['mqtt']['host']
    state.mqttState["port"]= config_data['mqtt']['port']
    state.httpState["host"]= config_data['http']['host']
    state.httpState["port"]= config_data['http']['port']

    parser = argparse.ArgumentParser(description='Robot MQTT Client')
    parser.add_argument('-name', '--robot_name', type=str, help='Name of the robot')
    args = parser.parse_args()
    if args.robot_name:
        state.mqttState["publish"]["notification"]   = "topic/notification/robot/"+ args.robot_name
        state.mqttState["publish"]["allData"]        = "topic/data/robot/"+ args.robot_name
        state.mqttState["publish"]["lastUpdate"]     = "topic/control/robot/"+args.robot_name+"/last-update"
        state.mqttState["subscribe"]["control"]      = "topic/control/robot/"+args.robot_name +"/property/+"
        state.mqttState["subscribe"]["lastUpdate"]   = "topic/data/robot/"+args.robot_name+"/last-update"
        state.mqttState["username"] = args.robot_name
        state.mqttState["password"] = args.robot_name
        state.robotState['robot'].name = args.robot_name
        state.mqttState["client_id"] = args.robot_name
        run()





        
    else:
        print("Please provide the name of the robot using -name or --robot_name argument.")

















"""
import requests
url = 'http://localhost:8089/management-robot-avg/robot/name/robot-1'
response = requests.get(url)
if response.status_code == 200:
    data = response.json()  # Assuming the response contains JSON data
    print(data)
else:
    print(f"Failed to retrieve data: {response.status_code}")"""







"""
# Usage

state.robotState[ "robot"].subscribe("name", on_name_change)
state.robotState[ "robot"].subscribe("statusRobot", on_status_change)
state.robotState[ "robot"].subscribe("levelBattery", on_battery_change)

# Access the values
print(f"Current name: {state.robotState['robot'].name}")
print(f"Current status: {state.robotState['robot'].statusRobot}")
print(f"Current battery level: {state.robotState['robot'].levelBattery}")

# Change the values
state.robotState[ "robot"].name = "new_robot"
state.robotState[ "robot"].statusRobot = "STOPPED"
state.robotState[ "robot"].levelBattery = 50"""