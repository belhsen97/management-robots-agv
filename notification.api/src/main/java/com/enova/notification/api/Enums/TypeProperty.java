package com.enova.notification.api.Enums;

public enum TypeProperty {
    CONNECTION(0),
    STATUS_ROBOT(1),
    MODE_ROBOT(2),
    OPERATION_STATUS(3),
    LEVEL_BATTERY(4),
    SPEED(5),
    DISTANCE(6),
    TAGCODE(7);
    private final int value;
    TypeProperty(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }

    public static  int parseValue(TypeProperty type)  {
        switch (type) {
            case CONNECTION:
                return CONNECTION.getValue();
            case STATUS_ROBOT:
                return STATUS_ROBOT.getValue();
            case MODE_ROBOT:
                return MODE_ROBOT.getValue();
            case OPERATION_STATUS:
                return OPERATION_STATUS.getValue();
            case LEVEL_BATTERY:
                return LEVEL_BATTERY.getValue();
            case SPEED:
                return SPEED.getValue();
            case DISTANCE:
                return DISTANCE.getValue();
            case TAGCODE:
                return TAGCODE.getValue();
            default:
                throw new IllegalArgumentException("Unknown TypeProperty: " + type.name());
        }
    }
    public static  int parseValue(String str)  {
        switch (str.toUpperCase()) {
            case "CONNECTION":
                return CONNECTION.getValue();
            case "STATUS_ROBOT":
                return STATUS_ROBOT.getValue();
            case "MODE_ROBOT":
                return MODE_ROBOT.getValue();
            case "OPERATION_STATUS":
                return OPERATION_STATUS.getValue();
            case "LEVEL_BATTERY":
                return LEVEL_BATTERY.getValue();
            case "SPEED":
                return SPEED.getValue();
            case "DISTANCE":
                return DISTANCE.getValue();
            case "TAGCODE":
                return TAGCODE.getValue();
            default:
                throw new IllegalArgumentException("Unknown TypeProperty: " + str);
        }
    }

}