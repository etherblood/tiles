package com.etherblood.network;

import com.etherblood.collections.IntKeyValueIterator;
import com.etherblood.collections.IntToIntHashMap;
import com.etherblood.collections.IntToIntMap;
import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.nio.ByteBuffer;

public class IntToIntMapSerializer extends Serializer {

    @Override
    public <T> T readObject(ByteBuffer data, Class<T> c) throws IOException {
        IntToIntMap map = new IntToIntHashMap();
        int size = data.getInt();
        for (int i = 0; i < size; i++) {
            map.set(data.getInt(), data.getInt());
        }
        return (T) map;
    }

    @Override
    public void writeObject(ByteBuffer buffer, Object object) throws IOException {
        IntToIntMap map = (IntToIntMap) object;
        buffer.putInt(map.size());
        IntKeyValueIterator iterator = map.keyValueIterator();
        while (iterator.next()) {
            buffer.putInt(iterator.key());
            buffer.putInt(iterator.value());
        }
    }

}
