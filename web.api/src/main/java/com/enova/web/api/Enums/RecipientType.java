package com.enova.web.api.Enums;



public  enum RecipientType  {
    TO("TO"),
    CC("CC"),
    BCC("BCC");
    private final String value;
    RecipientType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
    public static  String parseValue(String str)  {
        switch (str.toUpperCase()) {
            case "TO":
                return TO.getValue();
            case "CC":
                return CC.getValue();
            default:
                return BCC.getValue();
        }
    }
}