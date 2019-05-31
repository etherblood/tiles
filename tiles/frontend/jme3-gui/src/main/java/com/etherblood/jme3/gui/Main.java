package com.etherblood.jme3.gui;

import com.etherblood.core.to.GameConfig;
import com.etherblood.game.client.GameClient;
import com.etherblood.mods.core.game.DefaultMod;
import com.etherblood.network.GamesListMessage;
import com.etherblood.network.NetworkUtil;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.system.AppSettings;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class Main {

    public static void main(String... args) throws IOException, InterruptedException {
        NetworkUtil.initSerializers();

//        startClient(0);
        startClient(1);
    }

    private static void startClient(int playerIndex) throws IOException {
        GameClient client = new GameClient("localhost", NetworkUtil.DEFAULT_PORT);
        client.registerMod("default", new DefaultMod());
        TilesApplication app = new TilesApplication(client);
        app.setShowSettings(false);
        app.setPauseOnLostFocus(false);
        app.setSettings(defaultSettings());
        app.start();
        client.getClient().addMessageListener(new MessageListener<Client>() {
            @Override
            public void messageReceived(Client source, Message m) {
                GamesListMessage message = (GamesListMessage) m;
                Map.Entry<UUID, GameConfig> entry = message.getGames().entrySet().iterator().next();

                client.login(entry.getValue().getPlayers()[playerIndex].getId());
                client.join(entry.getKey());
                app.enqueue(() -> {
                    app.getStateManager().getState(GameSelectAppstate.class).selectGame(client.getGame(entry.getKey()));
                });
            }
        }, GamesListMessage.class);
        client.run();
    }

    private static AppSettings defaultSettings() {
        AppSettings settings = new AppSettings(true);
        settings.setResizable(true);
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Tiles");
        settings.setVSync(true);
        settings.setFrameRate(60);
        return settings;
    }

}
