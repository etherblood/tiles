package com.etherblood.network;

import com.etherblood.collections.IntToIntMap;
import com.etherblood.core.Action;
import com.etherblood.core.to.EntityComponentValue;
import com.etherblood.core.to.GameConfig;
import com.etherblood.core.to.HistoryAction;
import com.etherblood.core.to.ModConfig;
import com.etherblood.core.to.PlayerConfig;
import com.etherblood.core.to.RegistryState;
import com.jme3.network.serializing.Serializer;
import com.jme3.network.serializing.serializers.EnumSerializer;
import com.jme3.network.serializing.serializers.FieldSerializer;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkUtil {

    private static final Logger LOG = LoggerFactory.getLogger(NetworkUtil.class);
    public static final int DEFAULT_PORT = 6782;

    public static void initSerializers() {
        Serializer.registerClass(UUID.class, new UUIDSerializer());
        Serializer.registerClass(GameMessage.class);
        Serializer.registerClass(GamesListMessage.class);
        Serializer.registerClass(GameRequest.class, new EnumSerializer());
        Serializer.registerClass(IntToIntMap.class, new IntToIntMapSerializer());
        Serializer.registerClass(Action.class, new ActionSerializer());
        Serializer.registerClass(HistoryAction.class, new HistoryActionSerializer());
        FieldSerializer fieldSerializer = new FieldSerializer();
        Serializer.registerClass(GameConfig.class, fieldSerializer);
        Serializer.registerClass(ModConfig.class, fieldSerializer);
        Serializer.registerClass(PlayerConfig.class, fieldSerializer);
        Serializer.registerClass(RegistryState.class, fieldSerializer);
        Serializer.registerClass(EntityComponentValue.class, fieldSerializer);
        Serializer.registerClass(LoginMessage.class, fieldSerializer);
    }
}
