package cn.asany.email.utils;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileHandler;

public class ConfigLoader {
  public static XMLConfiguration getConfig(InputStream configStream) throws ConfigurationException {
    FileBasedConfigurationBuilder<XMLConfiguration> builder =
        new FileBasedConfigurationBuilder<>(XMLConfiguration.class)
            .configure(new Parameters().xml());
    try {
      XMLConfiguration xmlConfiguration = builder.getConfiguration();
      FileHandler fileHandler = new FileHandler(xmlConfiguration);
      fileHandler.load(configStream);
      try {
        configStream.close();
      } catch (IOException ignored) {
        // Ignored
      }
      return xmlConfiguration;
    } catch (Exception e) {
      e.printStackTrace();
      throw new ConfigurationException(e);
    }
  }
}
