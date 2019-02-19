package com.etherblood.jme.client;

import com.jme3.system.AppSettings;

/**
 *
 * @author Philipp
 */
public class Main {

    static {
        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String... args) {
        MyApp myApp = new MyApp();
        myApp.setShowSettings(false);
        myApp.setSettings(defaultSettings());
        myApp.start();
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
