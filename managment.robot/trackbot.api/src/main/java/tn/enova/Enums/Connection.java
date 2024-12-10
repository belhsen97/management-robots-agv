package tn.enova.Enums;

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

    public static int parseValue(String str) {
        switch (str.toUpperCase()) {
            case "CONNECTED":
                return CONNECTED.getValue();
            default:
                return DISCONNECTED.getValue();
        }
    }
}
