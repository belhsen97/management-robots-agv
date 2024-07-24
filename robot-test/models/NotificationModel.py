import json
from enums import NotificationEnum as notification_enum
class ReactiveNotification:
    def __init__(self, name, level, message, asctime):
        self._name = name
        self._level = level
        self._message = message
        self._asctime = asctime
        self._callbacks = {
            "name": [],
            "level": [],
            "message": [],
            "asctime": [],
            "notification": []
        }


    def _notify(self, key, new_value):
        for callback in self._callbacks[key]:
            callback(new_value)

    def subscribe(self, key, callback):
        if key in self._callbacks:
            self._callbacks[key].append(callback)


    def __call__(self, name, level, message, asctime):
        if self._message != message:
              self._notify("notification", ReactiveNotification (name, level, message, asctime))

    @property
    def name(self):
        return self._name

    @name.setter
    def name(self, value):
        if self._name != value:
            self._name = value
            self._notify("name", value)

    @property
    def level(self):
        return self._level

    @level.setter
    def level(self, value : notification_enum.LevelType):
        if self._level!= value:
            self._level= value
            self._notify("level", value)

    @property
    def message(self):
        return self._message

    @message.setter
    def message(self, value):
        if self._message != value:
            self._message = value
            self._notify("message", value)

    @property
    def asctime(self):
        return self._asctime

    @asctime.setter
    def asctime(self, value):
        if self._asctime != value:
            self._asctime= value
            self._notify("asctime", value)

    def toString(self) -> str:
        return "{name: "+self._name+" , level: "+self._level.name+" , message: "+self._message+" ,asctime: "+self._asctime+" }"
         
    def toSerialisation(self):
        data = {
            "name": self._name,
            "level": self._level.name,
            "message": self._message,
            "asctime": self._asctime
        }
        return json.dumps(data)