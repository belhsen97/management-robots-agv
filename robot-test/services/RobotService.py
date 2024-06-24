import random
import time
from paho.mqtt import client as mqtt_client
from datetime import datetime, timedelta
import json
import random_robot
import gc
from states import GlobalState as global_state


class RobotService:
    def __init__(self):
        print("\n RobotService init")

    def __enter__(self):
        print("\n RobotService enter")
        return self
        
    def __exit__(self, exc_type, exc_value, traceback):
        print("\n RobotService __exit__")
 
    def __del__(self):
        print("\n RobotService__del__")

    def updatePropertie(self,topic):
            parts = topic.split('/')
            lengthParts = len(parts)
            if lengthParts != 6:
               return
            print(f"attribut  `{parts[lengthParts-2]}` value: `{parts[lengthParts-1]}`")
            if not( (parts[lengthParts-2] == "MODE_ROBOT") or ( parts[lengthParts-2] == "OPERATION_STATUS")):
               return
            if (parts[lengthParts-2] == "MODE_ROBOT") and ((parts[lengthParts-1] == "MANUAL")or(parts[lengthParts-1] == "AUTO")) :
                 global_state.robotState['robot'].modeRobot = parts[lengthParts-1]
            if (parts[lengthParts-2] == "OPERATION_STATUS") and ((parts[lengthParts-1] == "NORMAL")or(parts[lengthParts-1] == "EMS")or(parts[lengthParts-1] == "PAUSE")) :
                 global_state.robotState['robot'].operationStatus = parts[lengthParts-1]

    def randomData(self):
           random_robot.update_battery_level()
           random_robot.update_speed_value()
           global_state.robotState['robot'].createdAt =  datetime.now().isoformat()
           global_state.robotState['robot'].levelBattery =  random_robot.battery_level 
           global_state.robotState['robot'].speed =  random_robot.speed_value