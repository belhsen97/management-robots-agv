import random
import time
from datetime import datetime
import gc
from states import GlobalState as state
from services import ThreadService as thread_service
from enums import RobotEnum as robot_enum
import requests

class RobotService:
    def __init__(self):
        print("\n RobotService init")
        state.task["speed"] =  thread_service.thread_with_trace(target = self.modifySpeed,args=(state.robotState['robot'].speed,)) 
        state.task["distance"] =  thread_service.thread_with_trace(target = self.modifyDistance) 
        state.task["battery"] =  thread_service.thread_with_trace(target = self.modifyBattery,args=(False, state.robotState['batteryConfig']['disChargeOnWaitingTime']    ,))
    def __enter__(self):
        print("\n RobotService enter")
        return self
        
    def __exit__(self, exc_type, exc_value, traceback):
        print("\n RobotService __exit__")
 
    def __del__(self):
        print("\n RobotService __del__")

    def updatePropertie(self,topic , data):
            parts = topic.split('/')
            lengthParts = len(parts)
            if lengthParts != 6:
               return
            key = parts[lengthParts-1]
            if not isinstance(data, dict) or "value" not in data:
               return
            value = data[ "value"]
            print(f"attribut  `{key}` value: `{value}`")
            if not( (key == "MODE_ROBOT") or ( key == "OPERATION_STATUS") or ( key == "STATUS") or ( key == "SPEED")):
               return
            if (key == "MODE_ROBOT") and ((value ==  robot_enum.ModeRobot.MANUAL.name)or(value == robot_enum.ModeRobot.AUTO.name)) :
                 state.robotState['robot'].modeRobot = value
            if (key  == "OPERATION_STATUS") and ((value == robot_enum.OperationStatus.NORMAL.name )or(value  ==  robot_enum.OperationStatus.EMS.name)or(value ==  robot_enum.OperationStatus.PAUSE.name)) :
                 print (value)
                 state.robotState['robot'].operationStatus = value
                 #print (  state.robotState['robot'].operationStatus )
            #if (key  == "STATUS") and ((value == robot_enum.Status.INACTIVE.name )or(value  == robot_enum.Status.WAITING.name )or(value == robot_enum.Status.RUNNING.name )) :
            if (key  == "STATUS") and ((value == robot_enum.Status.INACTIVE.name )) :
                 state.robotState['robot'].statusRobot = value
                 print(value)
            if (key  == "SPEED") and  isinstance(value, (int, float)):
                 state.robotState['speedTarget'] = float(value)
                 if state.robotState['robot'].statusRobot == robot_enum.Status.RUNNING.name:
                    state.task["speed"].kill()
                    state.task["speed"] = thread_service.thread_with_trace(target = self.modifySpeed,args=( state.robotState['speedTarget'],)) 
                    state.task["speed"].start()

    def updateAll(self,robot):
         if not ("connection" or "statusRobot" or "modeRobot"   or "operationStatus"  or "levelBattery"  or "speed"  or "distance"   or "codeTag"    in robot ):
              print ( "<------------   no attribute robot   ------------>" )
         if "connection" in robot:
              state.robotState['robot'].connection = robot["connection"]
         if "modeRobot" in robot:
              state.robotState['robot'].modeRobot = robot["modeRobot"]
         if "levelBattery" in robot:
              state.robotState['robot'].levelBattery = robot["levelBattery"]
         if "speed" in robot:
              #state.robotState['speedTarget'] = robot["speed"]
              state.robotState['robot'].speed = robot["speed"]
         if "statusRobot" in robot:
              state.robotState['robot'].statusRobot = robot["statusRobot"]
              print ( state.robotState['robot'].statusRobot)
         if "operationStatus" in robot:
              if robot["operationStatus"] == robot_enum.Status.INACTIVE.name:
                 state.robotState['robot'].operationStatus = robot_enum.Status.WAITING.name
              else:
                 state.robotState['robot'].operationStatus = robot["operationStatus"]
         if "createdAt" in robot:
              state.robotState['robot'].createdAt = robot["createdAt"]
         if "codeTag" in robot:
              state.robotState['robot'].codeTag = robot["codeTag"]
         if "distance" in robot:
              state.robotState['robot'].distance = robot["distance"]
 
       





    def modifyBattery(self, isDisCharging,  durationChangeTimeSecond): 
        last_update_time = datetime.now()
        while True :
           elapsed_time = (datetime.now() - last_update_time).total_seconds()
           div = (100/durationChangeTimeSecond ) 
           if  elapsed_time <= durationChangeTimeSecond and  not ((isDisCharging and state.robotState['robot'].levelBattery <= 0 )  or  ( (not isDisCharging) and state.robotState['robot'].levelBattery >= 100 )):
               state.robotState['robot'].levelBattery += -div if isDisCharging  else div 
           time.sleep(1)
       
    def modifySpeed(self, speedTarget):
        if  state.robotState['robot'].speed == speedTarget:
            return
        speed = state.robotState['robot'].speed 
        while speed <  speedTarget:
                  speed += state.robotState['accelaration']
                  if speed > speedTarget:
                           speed = speedTarget
                  state.robotState['robot'].speed = speed
                  time.sleep(1)
        while speed >  speedTarget:
                  speed -= state.robotState['accelaration'] 
                  if  speed < speedTarget:
                           speed = speedTarget
                  state.robotState['robot'].speed = speed
                  time.sleep(1)
      
    def modifyDistance(self):
        while state.robotState['robot'].speed !=  0:
              state.robotState['robot'].distance  =    round(state.robotState['robot'].distance + 0.1000 , 4)
              div = 0.1000/ state.robotState['robot'].speed
              time.sleep(div)

    def modifyCodeTag( self ):
         marge  =  state.robotState['robot'].distance -   state.tagCodeState["lastDistance"] 
         length = len(state.tagCodeState["list"])
         try:
            index = state.tagCodeState["list"].index(state.robotState['robot'].codeTag)
            if marge >= state.tagCodeState["distanceBetweenTags"]:
               state.tagCodeState["lastDistance"] =  state.robotState['robot'].distance 
               state.robotState['robot'].codeTag =  state.tagCodeState["list"][0] if index+1 >=length else  state.tagCodeState["list"][index+1]
            
            print(f"The index of '{state.robotState['robot'].codeTag}' is: {index}")
         except ValueError:
            print(f"'{state.robotState['robot'].codeTag}' is not in the list.")




    def getRobotCodes(self):
        url = 'http://'+state.httpState["host"]+':'+str(state.httpState["port"])+'/management-robot/driveless-service/tag/code-all'
        try:
           response = requests.get(url)
           response.raise_for_status()
           codes = response.json()
           state.tagCodeState[ "list" ] = codes
           state.tagCodeState["lengthList"] =  len(codes)
        except requests.exceptions.HTTPError as http_err:
           print(f"HTTP error occurred: {http_err}")
           print("Status code:", response.status_code)
           print("Response body:", response.json())
           raise
        except Exception as err:
           print(f"Other error occurred: {err}")
           raise


    def getRobot(self,name):
        url = 'http://'+state.httpState["host"]+':'+str(state.httpState["port"])+'/management-robot/driveless-service/robot/name/'+name
        try:
           response = requests.get(url)
           response.raise_for_status()
           robot = response.json()
           self.updateAll(robot)
        except requests.exceptions.HTTPError as http_err:
           print(f"HTTP error occurred: {http_err}")
           print("Status code:", response.status_code)
           print("Response body:", response.json())
           raise
        except Exception as err:
           print(f"Other error occurred: {err}")
           raise

    def getTargetSpeed(self):
        url = 'http://'+state.httpState["host"]+':'+str(state.httpState["port"])+'/management-robot/driveless-service/robot-setting/speed/average'
        try:
           response = requests.get(url)
           response.raise_for_status()
           datatargetSpeed = response.json()
           state.robotState['speedTarget']  =  datatargetSpeed[ "value" ]
        except requests.exceptions.HTTPError as http_err:
           print(f"HTTP error occurred: {http_err}")
           print("Status code:", response.status_code)
           print("Response body:", response.json())
           raise
        except Exception as err:
           print(f"Other error occurred: {err}")
           raise


   #def randomData2(self):
        # if  ( state.robotState['robot'].operationStatus ==  robot_enum.OperationStatus.PAUSE.name ) or ( state.robotState['robot'].operationStatus ==  robot_enum.OperationStatus.EMS.name ) :
        #            await self.cancelTask(state.task["speed"])
        #            state.task["speed"] = asyncio.create_task( state.robotState["service"].modifySpeed(0))
        #            return 
        # state.robotState['robot'].speed = 0.1
        # state.robotState['robot'].levelBattery = 100
        # state.task["battery"] = asyncio.create_task(
        #      state.robotState["service"].modifyBattery(True, state.robotState["batteryConfig"]["running"])
        #      )
        # state.robotState['robot'].speed = 0.1
        # state.robotState['speedTarget'] = 0.3 
        # state.task["speed"].kill()
        # state.task["speed"] =  thread_service.thread_with_trace(target = self.modifySpeed,args=(0.3,)) 
        # state.task["speed"].start()
        # state.task["speed"].join()           