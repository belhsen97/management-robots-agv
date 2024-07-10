package com.enova.collector.api.Enums;

public enum TypeProperty {CONNECTION,STATUS_ROBOT,MODE_ROBOT,OPERATION_STATUS,LEVEL_BATTERY,SPEED,DISTANCE,TAGCODE;
    public static  TypeProperty parseType(String str)  {
        switch (str.toUpperCase()) {
            case "CONNECTION":
                return CONNECTION;
            case "STATUS_ROBOT":
                return STATUS_ROBOT;
            case "MODE_ROBOT":
                return MODE_ROBOT;
            case "OPERATION_STATUS":
                return OPERATION_STATUS;
            case "LEVEL_BATTERY":
                return LEVEL_BATTERY;
            case "SPEED":
                return SPEED;
            case "DISTANCE":
                return DISTANCE;
            case "TAGCODE":
                return TAGCODE;
            default:
                return null;
        }
    }
}
