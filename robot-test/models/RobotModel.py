import json
class ReactiveRobot:
    def __init__(self, name, connection, statusRobot, modeRobot, operationStatus, createdAt, levelBattery, speed):
        self._name = name
        self._connection = connection
        self._statusRobot = statusRobot
        self._modeRobot = modeRobot
        self._operationStatus = operationStatus
        self._createdAt = createdAt
        self._levelBattery = levelBattery
        self._speed = speed
        self._callbacks = {
            "name": [],
            "connection": [],
            "statusRobot": [],
            "modeRobot": [],
            "operationStatus": [],
            "createdAt": [],
            "levelBattery": [],
            "speed": []
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
            self._createdAt= value
            self._notify("createdAt", value)

    @property
    def levelBattery(self):
        return self._levelBattery

    @levelBattery.setter
    def levelBattery(self, value):
        if self._levelBattery != value:
            self._levelBattery = value
            self._notify("levelBattery", value)

    @property
    def speed(self):
        return self._speed

    @speed.setter
    def speed(self, value):
        if self._speed != value:
            self._speed = value
            self._notify("speed", value)
    
    def toSerialisation(self):
        data = {
            "name": self._name,
            "connection": self._connection,
            "statusRobot": self._statusRobot,
            "modeRobot": self._modeRobot,
            "operationStatus": self._operationStatus,
            "createdAt": self._createdAt,
            "levelBattery": self._levelBattery,
            "speed": self._speed
        }
        return json.dumps(data)