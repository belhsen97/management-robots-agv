package com.enova.web.api.Enums;

//public enum StatusRobot {WAITING,RUNNING,INACTIVE}
public enum StatusRobot {
    INACTIVE(0),
    WAITING(1),
    RUNNING(2);
    private final int value;
    StatusRobot(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}


