package ru.otus.hw.converter;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
public class LongToObjectIdConverter {
    public ObjectId convertLongToObjectId(long id) {
        byte[] array = new byte[12];
        var buffer = ByteBuffer.wrap(array);
        buffer.putLong(id);
        buffer.putInt(0);
        return new ObjectId(array);
    }
}
