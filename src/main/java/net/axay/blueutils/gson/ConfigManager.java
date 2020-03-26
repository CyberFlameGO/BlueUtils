package net.axay.blueutils.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class ConfigManager {

    /**
     * @param path the path where the config file is located
     * @param configClass sets the type of the config
     * @return returns an object of the type of {@param configClass}
     */
    public static Object getConfig(String path, Class<?> configClass) {

        File file = new File(path);
        if (!file.exists()) {
            return null;
        }

        Gson gson = new Gson();

        try (Reader reader = new FileReader(path)) {
            return gson.fromJson(reader, configClass);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * @param path the path where the file should be created
     * @param defaultConfig should be null if the config should be empty
     *                      should be an object if the config should have default content
     */
    public static void createNewConfig(String path, Object defaultConfig) {

        File file = new File(path);
        if (!file.exists()) {
            if (file.getParentFile().mkdir()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("Failed to create parent file for: " + path);
            }
        }

        if (defaultConfig != null) {

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(defaultConfig, writer);
            } catch (IOException | StackOverflowError e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * @param path the path to the file where the config should be stored in
     * @param config should be null if the config should be empty
     *               should be an object if the config should have content
     */
    public static void saveConfig(String path, Object config) {

        File file = new File(path);
        if (!file.exists()) {
            createNewConfig(path, config);
        } else {

            if (config != null) {

                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                try (FileWriter writer = new FileWriter(file)) {
                    gson.toJson(config, writer);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

    }

}