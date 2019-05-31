package com.etherblood.network;

import com.etherblood.core.Action;
import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ActionSerializer extends Serializer {

    private static final int TARGET_POSITION_NOT_NULL_FLAG = 1;

    @Override
    public <T> T readObject(ByteBuffer data, Class<T> c) throws IOException {
        int target = data.getInt();
        byte notNullFlags = data.get();
        Integer targetPosition = (notNullFlags & TARGET_POSITION_NOT_NULL_FLAG) != 0 ? Serializer.getSerializer(Integer.class).readObject(data, Integer.class) : null;
        return (T) new Action(target, targetPosition);
    }

    @Override
    public void writeObject(ByteBuffer buffer, Object object) throws IOException {
        Action action = (Action) object;
        buffer.putInt(action.getTargetSkill());
        byte notNullFlags = 0;
        if (action.getTargetPosition() != null) {
            notNullFlags |= TARGET_POSITION_NOT_NULL_FLAG;
        }
        buffer.put(notNullFlags);
        if (action.getTargetPosition() != null) {
            Serializer.getSerializer(Integer.class).writeObject(buffer, action.getTargetPosition());
        }
    }

}
