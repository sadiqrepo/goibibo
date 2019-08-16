package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

/** Created by sadiq on 30/07/19. */
public class ConfigReader {

  private static final Properties properties;

  static {
    properties = new Properties();
    try {
      properties.load(
          new FileInputStream(new File("./src/test/resources/properties/config.properties")));
      properties.load(
          new FileInputStream(new File("./src/test/resources/properties/testdata.properties")));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String get(String name) {
    return get(name, null);
  }

  public static String get(String name, String defaultValue) {
    return properties.getProperty(name, defaultValue);
  }
}
