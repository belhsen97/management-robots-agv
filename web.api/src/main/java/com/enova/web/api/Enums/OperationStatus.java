package com.enova.web.api.Enums;

public enum OperationStatus {
    NORMAL(0),//NONE
    EMS(1),
    PAUSE(2);
    private final int value;
    OperationStatus(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
