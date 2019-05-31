package com.etherblood.network;

import com.etherblood.core.Action;
import com.etherblood.core.to.HistoryAction;
import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.nio.ByteBuffer;

public class HistoryActionSerializer extends Serializer {

    @Override
    public <T> T readObject(ByteBuffer data, Class<T> c) throws IOException {
        int gameVersion = data.getInt();
        int playerIndex = data.getInt();
        Action action = Serializer.getSerializer(Action.class).readObject(data, Action.class);
        int[] randomHistory = Serializer.getSerializer(int[].class).readObject(data, int[].class);
        return (T) new HistoryAction(gameVersion, playerIndex, action, randomHistory);
    }

    @Override
    public void writeObject(ByteBuffer buffer, Object object) throws IOException {
        HistoryAction action = (HistoryAction) object;
        buffer.putInt(action.getGameVersion());
        buffer.putInt(action.getPlayerIndex());
        Serializer.getSerializer(Action.class).writeObject(buffer, action.getAction());
        Serializer.getSerializer(int[].class).writeObject(buffer, action.getRandomHistory());
    }

}
