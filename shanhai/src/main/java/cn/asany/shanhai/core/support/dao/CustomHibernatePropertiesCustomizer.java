package cn.asany.shanhai.core.support.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.hibernate.cfg.AvailableSettings;
import org.jfantasy.framework.util.FantasyClassLoader;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;

public class CustomHibernatePropertiesCustomizer implements HibernatePropertiesCustomizer {
  @Override
  public void customize(Map<String, Object> hibernateProperties) {
    Collection<ClassLoader> classLoaders = new ArrayList<>();
    classLoaders.add(FantasyClassLoader.getClassLoader());
    hibernateProperties.put(AvailableSettings.CLASSLOADERS, classLoaders);
  }
}
