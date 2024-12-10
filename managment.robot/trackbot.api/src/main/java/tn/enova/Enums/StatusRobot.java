package tn.enova.Enums;

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

    public static  int parseValue(String str)  {
        switch (str.toUpperCase()) {
            case "RUNNING":
                return RUNNING.getValue();
            case "WAITING":
                return WAITING.getValue();
            default:
                return INACTIVE.getValue();
        }
    }
}


