from models import RobotModel as robot_model
from datetime import datetime

mqttState =	{
  "broker" : 'localhost',
  "port" : 1883,
  "client_id": "robot",
  "username" : "robot",
  "password" : "robot",
  "publish":{"lastUpdate":"","allData":""},
  "subscribe":{"control":"","controlAll":"topic/control/robot/all/property/+","lastUpdate":""},
  "service":None
}

tagCodeState = {
    "list" :  ["code-0000-0001","code-0000-0002","code-0000-0003","code-0000-0004","code-0000-0005",
                "code-0000-0006","code-0000-0007","code-0000-0008","code-0000-0009","code-0000-00010",],
    "lengthList":10,
    "distanceBetweenTags" : 1,
    "lastDistance" : 0
}

robotState = {
        "robot":  robot_model.ReactiveRobot( "robot","DISCONNECTED", "WAITING","AUTO","PAUSE",datetime.now().isoformat(),100,0,0, tagCodeState[  "list" ][0]),
        "service":None,
        "batteryConfig":{ "disChargeTime": 60*60*2 ,"chargeTime" :60*30*1 , "isDisCharging": True},
        "speedTarget": 0.0,
        "accelaration": 0.01
}

task = {
    "battery":None,
    "speed":None, 
    "distance":None
}