package com.etherblood.network;

import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDSerializer extends Serializer {

    @Override
    public <T> T readObject(ByteBuffer data, Class<T> c) throws IOException {
        return (T) new UUID(data.getLong(), data.getLong());
    }

    @Override
    public void writeObject(ByteBuffer buffer, Object object) throws IOException {
        UUID uuid = (UUID) object;
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
    }

}
