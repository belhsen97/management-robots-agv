import json 
from states import GlobalState as state
from services import ThreadService as thread_service
from enums import RobotEnum as robot_enum
import os
def on_connection_change(value):
    print(f"connection changed to {value}")
    state.mqttState["service"].publish(state.mqttState["publish"]["allData"]+"/property/CONNECTION",json.dumps({"value": value}))

def on_mode_change(value):
    print(f"modeRobot changed to {value}")
    state.mqttState["service"].publish(state.mqttState["publish"]["allData"]+"/property/MODE_ROBOT",json.dumps({"value": value}))

def on_status_change(value):
    print(f"statusRobot changed to {value}")
    state.mqttState["service"].publish(state.mqttState["publish"]["allData"]+"/property/STATUS_ROBOT",json.dumps({"value": value}))
   
    if(value ==  robot_enum.Status.INACTIVE.name):
                state.task["speed"].kill()  
                state.robotState['robot'].speed=0
                #raise Exception('Close')
                #raise

def on_operation_status_change(value):
    print(f"operationStatus changed to {value}")
    state.mqttState["service"].publish(state.mqttState["publish"]["allData"]+"/property/OPERATION_STATUS",json.dumps({"value": value}))
    if (value == robot_enum.OperationStatus.NORMAL.name ):
                 state.task["speed"].kill()
                 state.task["speed"] = thread_service.thread_with_trace(target =  state.robotState["service"].modifySpeed,args=(state.robotState['speedTarget'],)) 
                 state.task["speed"].start()
    if (value  ==  robot_enum.OperationStatus.EMS.name)or(value ==  robot_enum.OperationStatus.PAUSE.name):
                 #state.robotState['speedTarget'] = state.robotState['robot'].speed
                 state.task["speed"].kill()
                 state.task["speed"] = thread_service.thread_with_trace(target =  state.robotState["service"].modifySpeed,args=(0,)) 
                 state.task["speed"].start()


def on_battery_change(value):
    print(f"levelBattery changed to {value}")
    state.mqttState["service"].publish(state.mqttState["publish"]["allData"]+"/property/LEVEL_BATTERY",json.dumps({"value": value}))
    if(value == 0 ):
          state.robotState['robot'].speed = 0
          state.robotState['robot'].statusRobot = robot_enum.Status.INACTIVE.name
def on_speed_change(value):
    print(f"speed changed to {value}")
    state.mqttState["service"].publish(state.mqttState["publish"]["allData"]+"/property/SPEED",json.dumps({"value": value}))
    state.task["distance"].kill()
    state.task["distance"] =  thread_service.thread_with_trace(target = state.robotState["service"].modifyDistance) 
    state.task["distance"].start()
    if(value == 0 ):
          state.robotState['robot'].statusRobot = robot_enum.Status.WAITING.name
    else: 
          state.robotState['robot'].statusRobot = robot_enum.Status.RUNNING.name 

def on_distance_change(value):
    print(f"distance changed to {value}")
    state.mqttState["service"].publish(state.mqttState["publish"]["allData"]+"/property/DISTANCE",json.dumps({"value": value}))
    state.robotState["service"].modifyCodeTag()

def on_code_tag_change(value):
    print(f"code tag changed to {value}")
    state.mqttState["service"].publish(state.mqttState["publish"]["allData"]+"/property/TAGCODE",json.dumps({"value": value}))
    try:
        index = state.tagCodeState["list"].index(state.robotState['robot'].codeTag)
        print (index )
        print (state.robotState['batteryConfig']['isDisCharging'] )
        if (index <  state.tagCodeState["lengthList"]/4 ) and   state.robotState['batteryConfig']['isDisCharging']:
                state.task["battery"].kill()
                state.task["battery"] = thread_service.thread_with_trace(target = state.robotState["service"].modifyBattery,args=(False, state.robotState['batteryConfig']['chargeTime'],))
                state.task["battery"].start()
                state.robotState['batteryConfig']['isDisCharging'] = False
        if (index >  state.tagCodeState["lengthList"]/4 ) and (not  state.robotState['batteryConfig']['isDisCharging']):
                time =  state.robotState['batteryConfig']['disChargeOnRunningTime'] if   state.robotState['robot'].speed  >  0 else  state.robotState['batteryConfig']['disChargeOnWaitingTime']
                state.task["battery"].kill()
                state.task["battery"] = thread_service.thread_with_trace(target = state.robotState["service"].modifyBattery,args=(True, time,))
                state.task["battery"].start()
                state.robotState['batteryConfig']['isDisCharging'] = True
    except ValueError:
        print(f"'{state.robotState['robot'].codeTag}' is not in the list.")