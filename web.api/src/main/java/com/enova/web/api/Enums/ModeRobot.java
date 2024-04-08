package com.enova.web.api.Enums;

public enum ModeRobot {
    MANUAL(0),
    AUTO(1);
    private final int value;
    ModeRobot(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
