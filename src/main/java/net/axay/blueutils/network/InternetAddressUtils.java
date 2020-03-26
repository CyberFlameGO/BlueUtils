package net.axay.blueutils.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class InternetAddressUtils {

    public static String getInetData(InetSocketAddress ip) throws Exception {
        return getInetData(ip, false);
    }

    public static String getInetData(InetSocketAddress ip, boolean pretty) throws Exception {

        URL url = new URL("http://ip-api.com/json/" + ip.getHostName());
        BufferedReader stream = new BufferedReader(
                new InputStreamReader(url.openStream(), StandardCharsets.UTF_8)
        );

        StringBuilder entirePage = new StringBuilder();

        String inputLine;
        while ((inputLine = stream.readLine()) != null) {
            entirePage.append(inputLine);
            if (pretty) entirePage.append("\n");
        }

        stream.close();

        return entirePage.toString();

    }

    public static String getStringFromInetDataField(StringBuilder page, String fieldName) {
        if (!(page.toString().contains("\"" + fieldName + "\":\""))) {
            return null;
        }
        return page.toString().split("\"" + fieldName + "\":\"")[1].split("\",")[0];
    }

}