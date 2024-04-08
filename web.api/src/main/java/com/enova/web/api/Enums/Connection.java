package com.enova.web.api.Enums;

public enum Connection {
    DISCONNECTED(0),
    CONNECTED(1);
    private final int value;
    Connection(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
