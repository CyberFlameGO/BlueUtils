package net.axay.blueutils.gson;

public class SimpleConfigManager {

    public static String getString(String path, String defaultValue) {
        String value = (String) ConfigManager.getConfig( path, String.class);
        if (value == null) {
            value = defaultValue;
            ConfigManager.createNewConfig(path, value);
        }
        return value;
    }

    public static String getString(String path) throws Exception {
        String value = (String) ConfigManager.getConfig( path, String.class);
        if (value == null) {
            value = "notset";
            ConfigManager.createNewConfig(path, value);
        }
        return value;
    }

    public static void saveString(String path, String value) {
        ConfigManager.saveConfig(path, value);
    }

}
