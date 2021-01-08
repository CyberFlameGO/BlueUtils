package net.axay.blueutils.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class provides static utility
 * functions for loading resources in classpath.
 */
public class ResourceUtils {

    /**
     * loads the content of the given file
     * @param fileName the classpath fileName
     * @param charset the charset which should be used to decode the resource
     * @return the content of the file as a String
     * @throws IOException if the file could not be loaded
     */
    public static String loadResource(String fileName, Charset charset) throws IOException {

        String result;
        try (
                InputStream inputStream = ResourceUtils.class.getResourceAsStream(fileName);
                Scanner scanner = new Scanner(inputStream, charset.name())
        ) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;

    }

    /**
     * does the same as as {@link #loadResource(String, Charset)}
     * but the charset defaults to {@link Charset#defaultCharset()}
     */
    public static String loadResource(String fileName) throws IOException {
        return loadResource(fileName, Charset.defaultCharset());
    }

    /**
     * loads the content of the given file by lines
     * @param fileName the classpath fileName
     * @param charset the charset which should be used to decode the resource
     * @return the content of the file as a List<String> representing the lines
     * @throws IOException if the file could not be loaded
     */
    public static List<String> loadResourceByLines(String fileName, Charset charset) throws IOException {

        List<String> lines = new ArrayList<>();

        try (
                InputStream inputStream = ResourceUtils.class.getResourceAsStream(fileName);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset))
        ) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        }

        return lines;

    }

    /**
     * does the same as as {@link #loadResourceByLines(String, Charset)}
     * but the charset defaults to {@link Charset#defaultCharset()}
     */
    public static List<String> loadResourceByLines(String fileName) throws IOException {
        return loadResourceByLines(fileName, Charset.defaultCharset());
    }

}