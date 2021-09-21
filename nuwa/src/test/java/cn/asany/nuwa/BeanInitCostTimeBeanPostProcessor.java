package cn.asany.nuwa;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Slf4j
public class BeanInitCostTimeBeanPostProcessor implements BeanPostProcessor {

  private static final ConcurrentHashMap<String, Long> START_TIME =
      new ConcurrentHashMap<String, Long>();

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    START_TIME.put(beanName, System.currentTimeMillis());
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    if (Objects.nonNull(START_TIME.get(beanName))) {
      log.info(
          "beanName: {}  cost: {}",
          beanName,
          System.currentTimeMillis() - START_TIME.get(beanName));
    }
    return bean;
  }
}
