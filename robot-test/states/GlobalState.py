from models import RobotModel as robot_model
from datetime import datetime

mqttState =	{
  "broker" : 'localhost',
  "port" : 1883,
  "client_id": "robot",
  "username" : "robot",
  "password" : "robot",
  "publish":{"allData":"topic/data/robot/"},
  "subscribe":{"control":"topic/control/robot/","controlAll":"topic/control/robot/all/+/+" }
}
robotState = {
        "robot":  robot_model.ReactiveRobot( "robot","DISCONNECTED", "RUNNING","AUTO","PAUSE",datetime.now().isoformat(),0,0)
}