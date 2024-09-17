package helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.GameInfo;

public class Debug {
    private static HashMap<String, Boolean> debugSettings = new HashMap<>();
    private static final Pattern VALID_SETTING_PATTERN = Pattern.compile("^([A-Z]\\d+)\\.\\s*([^:]+):\\s*(true|false)$");

    static {
        try(InputStream inputStream = Debug.class.getClassLoader().getResourceAsStream("documents/DebugSettings.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = VALID_SETTING_PATTERN.matcher(line);
                if (matcher.matches()) {
                    String settingID = matcher.group(1);
                    boolean isActive = Boolean.parseBoolean(matcher.group(3));
                    debugSettings.put(settingID, isActive);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean on(String settingID) {
        return debugSettings.getOrDefault(settingID, false) && GameInfo.debug();
    }

    public static boolean off(String settingID) {
        return !debugSettings.getOrDefault(settingID, false) && GameInfo.debug();
    }
}