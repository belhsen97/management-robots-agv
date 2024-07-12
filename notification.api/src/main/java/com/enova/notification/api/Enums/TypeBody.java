package com.enova.notification.api.Enums;

public enum TypeBody {
    PDF("application/pdf"),
    JPEG("image/jpeg"),
    PNG("image/png"),
    GIF("image/gif"),
    WEBP("image/webp"),
    AVIF("image/avif"),
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
