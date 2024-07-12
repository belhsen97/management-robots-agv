from models import RobotModel as robot_model
from datetime import datetime
from models import NotificationModel as notification_model
from configurations import LoggingConfiguration as logging_config



logging = logging_config.logger_config("robot-1","resources/logs","my_log.log")

notification = notification_model.ReactiveNotification ("name", "level", "message", "asctime")

mqttState =	{
  "broker" : 'localhost',
  "port" : 1883,
  "client_id": "robot",
  "username" : "robot",
  "password" : "robot",
  "publish":{"lastUpdate":"","allData":"","notification":""},
  "subscribe":{"control":"","controlAll":"topic/control/robot/all/property/+","lastUpdate":""},
  "service":None
}

httpState = {
  "host" : 'localhost',
  "port" : 8088,
}

tagCodeState = {
    "list" :  ["code-0000-0001","code-0000-0002","code-0000-0003","code-0000-0004","code-0000-0005",
                "code-0000-0006","code-0000-0007","code-0000-0008","code-0000-0009","code-0000-00010",],
    "lengthList":10,
    "distanceBetweenTags" : 1,
    "lastDistance" : 0
}

robotState = {
        "robot":  robot_model.ReactiveRobot( "robot","DISCONNECTED", "WAITING","AUTO",None,datetime.now().isoformat(),100,0,0, tagCodeState[  "list" ][0]),
        "service":None,
        "batteryConfig":{ "disChargeOnRunningTime": 60*60*4 , "disChargeOnWaitingTime": 60*60*8 , "chargeTime" :60*60*1 , "isDisCharging": True},
        "speedTarget": 0.0,
        "accelaration": 0.05
}

task = {
    "battery":None,
    "speed":None, 
    "distance":None
}