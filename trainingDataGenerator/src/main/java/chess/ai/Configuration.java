package chess.ai;

import java.io.*;
import java.util.Properties;

public class Configuration {
    public static final Properties prop = new Properties();
    private static String fileName = "/usr/src/trainingDataGenerator/config.d/trainingConfig";
    private static String defaultConfigFileName = "src/main/resources/static/trainingConfig";

    private static InputStream is = null;

    public static void Congifure() {

        try {
            is = new FileInputStream(fileName);
        } catch (FileNotFoundException exc) {
            exc.printStackTrace();
            SetDefault();
        }

        try {
            prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(prop.getProperty("moveEvaluatorURL"));
    }

    private static void SetDefault() {
        try {
            is = new FileInputStream(defaultConfigFileName);
        } catch (FileNotFoundException exc) {
            exc.printStackTrace();
        }

    }
}
