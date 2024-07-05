from states import GlobalState as global_state
import json
def onSubscribeControlRobot(msg):
      print(f"Subscribe Control Robot `{msg.payload.decode()}` from `{msg.topic}` topic")
      global_state.robotState["service"].updatePropertie(msg.topic,json.loads( msg.payload.decode())) #example "topic/robot/control/robot-1/modeRobot/AUTO"


def onSubscribeControlAllRobot(msg):
      print(f"Subscribe Control All Robot `{msg.payload.decode()}` from `{msg.topic}` topic")
      global_state.robotState["service"].updatePropertie(msg.topic,json.loads( msg.payload.decode())) #example "topic/robot/control/all/modeRobot/AUTO"

def onSubscribeInitDataRobot(msg):
      print(f"Subscribe int Robot `{msg.payload.decode()}` from `{msg.topic}` topic")
      global_state.robotState["service"].updateAll( json.loads(  msg.payload.decode()))