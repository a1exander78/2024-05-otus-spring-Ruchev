package ru.otus.hw.converter;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
public class ObjectIdToLongConverter {
    public long convertObjectIdToLong(ObjectId id) {
        var buffer = ByteBuffer.wrap(id.toByteArray());
        return buffer.getLong();
    }
}
