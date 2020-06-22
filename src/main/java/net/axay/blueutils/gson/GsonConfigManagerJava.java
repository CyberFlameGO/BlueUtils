package net.axay.blueutils.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;

@Deprecated
public class GsonConfigManagerJava {

    private static final Gson gson = new Gson();
    private static final Gson gsonPretty = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    /**
     * @param path the path where the config file is located
     * @param configClass sets the type of the config
     * @return returns an object of the type of {@param configClass}
     */
    @Nullable
    public static Object getConfig(@NotNull String path, Class<?> configClass) {

        File file = new File(path);
        if (!file.exists()) return null;

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
    public static void createNewConfig(@NotNull String path, Object defaultConfig) {

        File file = new File(path);
        if (!file.exists()) {
            if (!file.getParentFile().exists())
                if (!file.getParentFile().mkdir())
                    System.err.println("Failed to create parent file for: " + file.getAbsolutePath());
            try {
                if (!file.createNewFile())
                    System.err.println("Failed to create the following file: " + file.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Failed to create the following file: " + file.getAbsolutePath());
                e.printStackTrace();
            }
        }

        try (FileWriter writer = new FileWriter(file)) {
            gsonPretty.toJson(defaultConfig, writer);
        } catch (IOException | StackOverflowError exc) {
            System.err.println("Failed to save the data to the following file: " + file.getAbsolutePath());
            exc.printStackTrace();
        }

    }

    /**
     * @param path the path to the file where the config should be stored in
     * @param config should be null if the config should be empty
     *               should be an object if the config should have content
     */
    public static void saveConfig(@NotNull String path, Object config) {

        File file = new File(path);
        if (!file.exists()) {
            createNewConfig(path, config);
        } else {

            try (FileWriter writer = new FileWriter(file)) {
                gsonPretty.toJson(config, writer);
            } catch (IOException | StackOverflowError exc) {
                System.err.println("Failed to save the data to the following file: " + file.getAbsolutePath());
                exc.printStackTrace();
            }

        }

    }

}