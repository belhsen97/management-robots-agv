package com.enova.notification.api.Configures;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AsctimeSerializer extends StdSerializer<Long> {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

    public AsctimeSerializer() {
        this(null);
    }

    public AsctimeSerializer(Class<Long> t) {
        super(t);
    }

    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        String formattedDate = formatter.format(new Date(value));
        gen.writeString(formattedDate);
    }
}