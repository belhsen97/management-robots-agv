import json
import math


class ReactiveRobot:
    def __init__(self, name, connection, statusRobot, modeRobot, operationStatus, createdAt, levelBattery, speed, distance, codeTag):
        self._name = name
        self._connection = connection
        self._statusRobot = statusRobot
        self._modeRobot = modeRobot
        self._operationStatus = operationStatus
        self._createdAt = createdAt
        self._levelBattery = levelBattery
        self._speed = speed
        self._distance = distance
        self._codeTag = codeTag
        self._callbacks = {
            "name": [],
            "connection": [],
            "statusRobot": [],
            "modeRobot": [],
            "operationStatus": [],
            "createdAt": [],
            "levelBattery": [],
            "speed": [],
            "distance": [],
            "codeTag": []
        }

    def _notify(self, key, new_value):
        for callback in self._callbacks[key]:
            callback(new_value)

    def subscribe(self, key, callback):
        if key in self._callbacks:
            self._callbacks[key].append(callback)

    @property
    def name(self):
        return self._name

    @name.setter
    def name(self, value):
        if self._name != value:
            self._name = value
            self._notify("name", value)

    @property
    def connection(self):
        return self._connection

    @connection.setter
    def connection(self, value):
        if self._connection != value:
            self._connection = value
            self._notify("connection", value)

    @property
    def statusRobot(self):
        return self._statusRobot

    @statusRobot.setter
    def statusRobot(self, value):
        if self._statusRobot != value:
            self._statusRobot = value
            self._notify("statusRobot", value)

    @property
    def modeRobot(self):
        return self._modeRobot

    @modeRobot.setter
    def modeRobot(self, value):
        if self._modeRobot != value:
            self._modeRobot = value
            self._notify("modeRobot", value)

    @property
    def operationStatus(self):
        return self._operationStatus

    @operationStatus.setter
    def operationStatus(self, value):
        if self._operationStatus != value:
            self._operationStatus = value
            self._notify("operationStatus", value)

    @property
    def createdAt(self):
        return self._createdAt

    @createdAt.setter
    def createdAt(self, value):
        if self._createdAt != value:
            self._createdAt = value
            self._notify("createdAt", value)

#<-----------------------------   battery 0-100(%)   ----------------------------->
    @property
    def levelBattery(self):
        return  self._levelBattery
    @levelBattery.setter
    def levelBattery(self, value):
        if not isinstance(value, (int, float)):
            raise ValueError("Speed must be a number.")
        value = math.floor(value * 100) / 100.0
        value = 100.0 if value>100  else 0.0 if value<0 else value
        if self._levelBattery != value:
            self._levelBattery = value
            self._notify("levelBattery", value)

#<-----------------------------   speed 0-0.3(m/s)   ----------------------------->
    @property
    def speed(self):
        return self._speed
    @speed.setter
    def speed(self, value):
        if not isinstance(value, (int, float)):
            raise ValueError("Speed must be a number.")
        value = math.floor(value * 100) / 100.0
        value = 0.3 if value>0.3  else 0.0 if value<0 else value
        if self._speed != value:
            self._speed = value
            self._notify("speed", value)

#<-----------------------------   distance (m)  ----------------------------->
    @property
    def distance(self):
        return self._distance
    @distance.setter
    def distance(self, value):
        if not isinstance(value, (int, float)):
            raise ValueError("Distance must be a number.")
        value = math.floor(value * 10000) / 10000.0
        if self._distance != value:
            self._distance = value
            self._notify("distance", value)

#<-----------------------------   tag code-0000-0001  ----------------------------->
    @property
    def codeTag(self):
        return self._codeTag
    @codeTag.setter
    def codeTag(self, value):
        if self._codeTag != value:
            self._codeTag = value
            self._notify("codeTag", value)


    def toSerialisation(self):
        data = {
            "name": self._name,
            "connection": self._connection,
            "statusRobot": self._statusRobot,
            "modeRobot": self._modeRobot,
            "operationStatus": self._operationStatus,
            "createdAt": self._createdAt,
            "levelBattery": self._levelBattery,
            "speed": self._speed,
            "distance": self._distance,
            "codeTag": self._codeTag
        }
        return json.dumps(data)
