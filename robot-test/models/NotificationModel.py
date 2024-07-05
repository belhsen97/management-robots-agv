import json
class ReactiveNotification:
    def __init__(self, sender, displayType, statusRobot, severity, description, createdAt, expiredAt):
        self._sender = sender
        self.displayType = displayType
        self._statusRobot = statusRobot
        self._severity = severity
        self._description = description
        self._createdAt = createdAt
        self.expiredAt = expiredAt
        self._callbacks = {
            "sender": [],
            "displayType": [],
            "statusRobot": [],
            "severity": [],
            "description": [],
            "createdAt": [],
            "expiredAt": []
        }

    def _notify(self, key, new_value):
        for callback in self._callbacks[key]:
            callback(new_value)

    def subscribe(self, key, callback):
        if key in self._callbacks:
            self._callbacks[key].append(callback)

    @property
    def sender(self):
        return self._sender

    @sender.setter
    def sender(self, value):
        if self._sender != value:
            self._sender = value
            self._notify("sender", value)

    @property
    def displayType(self):
        return self._displayType

    @displayType.setter
    def displayType(self, value):
        if self._displayType!= value:
            self._displayType= value
            self._notify("displayType", value)

    @property
    def statusRobot(self):
        return self._statusRobot

    @statusRobot.setter
    def statusRobot(self, value):
        if self._statusRobot != value:
            self._statusRobot = value
            self._notify("statusRobot", value)

    @property
    def severity(self):
        return self._severity

    @severity.setter
    def severity(self, value):
        if self._severity!= value:
            self._severity= value
            self._notify("severity", value)

    @property
    def description(self):
        return self._description

    @description.setter
    def description(self, value):
        if self._description != value:
            self._description = value
            self._notify("description", value)

    @property
    def createdAt(self):
        return self._createdAt

    @createdAt.setter
    def createdAt(self, value):
        if self._createdAt != value:
            self._createdAt= value
            self._notify("createdAt", value)

    @property
    def expiredAt(self):
        return self._expiredAt

    @expiredAt.setter
    def expiredAt(self, value):
        if self._expiredAt != value:
            self._expiredAt = value
            self._notify("expiredAt", value)

    
    def toSerialisation(self):
        data = {
            "sender": self._sender,
            "displayType": self._displayType,
            "statusRobot": self._statusRobot,
            "severity": self._severity,
            "description": self._description,
            "createdAt": self._createdAt,
            "expiredAt": self._expiredAt
        }
        return json.dumps(data)