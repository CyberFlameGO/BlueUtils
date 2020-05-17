package net.axay.blueutils.gson;

public class SimpleGsonConfigManager {

    public static String getString(String path, String defaultValue) {
        String value = (String) GsonConfigManager.getConfig( path, String.class);
        if (value == null) {
            value = defaultValue;
            GsonConfigManager.createNewConfig(path, value);
        }
        return value;
    }

    public static String getString(String path) throws Exception {
        String value = (String) GsonConfigManager.getConfig( path, String.class);
        if (value == null) {
            value = "notset";
            GsonConfigManager.createNewConfig(path, value);
        }
        return value;
    }

    public static void saveString(String path, String value) {
        GsonConfigManager.saveConfig(path, value);
    }

}