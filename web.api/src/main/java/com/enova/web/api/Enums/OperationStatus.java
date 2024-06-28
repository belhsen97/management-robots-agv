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

    public static int parseValue(String str) {
        switch (str.toUpperCase()) {
            case "PAUSE":
                return PAUSE.getValue();
            case "EMS":
                return EMS.getValue();
            default:
                return NORMAL.getValue();
        }
    }
}
