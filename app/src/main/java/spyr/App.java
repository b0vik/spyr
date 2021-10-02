/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package spyr;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.google.gson.Gson;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;
import spyr.configStorage.ConfigManager;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    public static MainWindow mainWindow;
    public static AudioPlayer audioPlayer;
    static AppDirs appDirs = AppDirsFactory.getInstance();
    public static String configDir = appDirs.getUserConfigDir("Spyr", null, null);
    public static File songsJson;
    public static File configJson;
    public static ConfigManager appConfigManager;
    public static EmbeddedMediaPlayerComponent embeddedMediaPlayerComponent;
    public static void openConfig() {
        File configDirLocation = new File(configDir);
        configJson = new File(configDir + "/config.json");
        songsJson = new File(configDir + "/songs.json");
        try {
            if (configDirLocation.mkdir()) {
                songsJson.createNewFile();
                configJson.createNewFile();
                System.out.println("Config file doesn't exist, creating...");
                Writer configWriter = Files.newBufferedWriter(Paths.get(configDir + "/config.json"));
                Writer songsWriter = Files.newBufferedWriter(Paths.get(configDir + "/songs.json"));
                configWriter.write(ConfigManager.createDefaultConfig());
                songsWriter.write(ConfigManager.createTestSongsConfig());
                songsWriter.close();
                configWriter.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error creating config files! Do you have the required permissions?");
        }
    }
    public static void writeOutJson() {
        //todo: this entire method does a great job of showcasing how little I know about java OOP
        mainWindow.songManager.configManager.sortSongJson();
        songsJson.delete();
        configJson.delete();
        try {
            songsJson.createNewFile();
            configJson.createNewFile();
            Writer songsWriter = Files.newBufferedWriter(Paths.get(configDir + "/songs.json"));
            Writer configWriter = Files.newBufferedWriter(Paths.get(configDir + "/config.json"));
            songsWriter.write(mainWindow.songManager.configManager.getJson());
            configWriter.write(appConfigManager.getAppConfigJson());
            songsWriter.close();
            configWriter.close();
        } catch (Exception ignored) { }
    }
    public static void main(String[] args) {
        audioPlayer = new AudioPlayer();
        embeddedMediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        openConfig();
        System.out.println("Configuration will be stored at " + configDir);

        appConfigManager = new ConfigManager();
        try {
            if (appConfigManager.getIsDarkMode()) {
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        mainWindow = new MainWindow();
        mainWindow.setup();
    }
}
