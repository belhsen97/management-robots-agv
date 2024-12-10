from configurations import GlobalConfiguration
from services import MqttService as mqtt_service
from services import RobotService as robot_service
from states import GlobalState as state
from effects import RobotEffect as robot_effect
from effects import MqttEffect as mqtt_effect
from effects import NotificationEffect as notification_effect
from configurations import LoggingConfiguration as logging_config
from enums import RobotEnum as robot_enum

from datetime import datetime
import argparse
import time

# robotService : robot_service.RobotService
# clientMqttService : mqtt_service.MqttService

def run():
        exit_flag = False
        state.notification.subscribe("notification", notification_effect.on_send_notification)
        state.notification.subscribe("notification", notification_effect.on_save_log_notification)

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
        
        state.robotState["service"].getTargetSpeed( )
        state.robotState["service"].getRobotCodes()
        state.robotState["service"].getRobot( state.robotState['robot'].name )

        # while  not state.mqttState["service"].is_connected() :
        #        print (state.mqttState["publish"]["lastUpdate"]  )
        #        state.mqttState["service"].publish( state.mqttState["publish"]["lastUpdate"] ,"vide")
        #        time.sleep(1)
        while  not exit_flag:
     
             #state.notification("name1", notification_enum.LevelType.INFO, "message"+str(count))
             #if state.robotState['robot'].operationStatus != robot_enum.Status.INACTIVE.name : 
             #       exit_flag = True
             time.sleep(1)

       








if __name__ == '__main__':
    #GlobalConfiguration.create_config('config.ini')
    
    state.configure = GlobalConfiguration.read_config('config.ini')
    # Print the retrieved values
    # print("Debug Mode:", state.configure['debug_mode'])
    # print("Log Level:", state.configure['log_level'])
    print("MQTT Name:", state.configure['mqtt']['host'])
    print("MQTT port:", state.configure['mqtt']['port'])
    print("HTTP Name:", state.configure['http']['host'])
    print("HTTP port:", state.configure['http']['port'])
    state.mqttState["broker"]= state.configure['mqtt']['host']
    state.mqttState["port"]= state.configure['mqtt']['port']
    state.httpState["host"]= state.configure['http']['host']
    state.httpState["port"]= state.configure['http']['port']

    parser = argparse.ArgumentParser(description='Robot MQTT Client')
    parser.add_argument('-name', '--robot_name', type=str, help='Name of the robot')
    args = parser.parse_args()
    if args.robot_name:
        state.logger = logging_config.logger_config(args.robot_name,state.configure['LOG']['src'],f"{args.robot_name}.log")
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