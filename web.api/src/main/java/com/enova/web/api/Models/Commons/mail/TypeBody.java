package com.enova.web.api.Models.Commons.mail;

public enum TypeBody {
    PDF("application/pdf"),
    JPEG("image/jpeg"),
    PNG("image/png"),
    DOC("application/msword"),
    XLS("application/vnd.ms-excel"),
    TXT("text/plain"),
    HTML("text/html");
    private final String mimeType;

    TypeBody(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }
}
