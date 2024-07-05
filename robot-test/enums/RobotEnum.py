from enum import Enum

class Status(Enum):
    INACTIVE = 0
    WAITING = 1
    RUNNING = 2
#print (Status.ACTIVE.value) # name value

class Connection(Enum):
    DISCONNECTED = 0
    CONNECTED = 1

class ModeRobot(Enum):
    MANUAL = 0
    AUTO = 1

class OperationStatus(Enum):
    NORMAL = 0
    EMS = 1
    PAUSE = 2
