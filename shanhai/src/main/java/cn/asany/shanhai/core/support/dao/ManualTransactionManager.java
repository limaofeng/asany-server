package cn.asany.shanhai.core.support.dao;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.jfantasy.framework.error.ValidationException;

/**
 * 手动事务管理器
 *
 * @author limaofeng
 */
@Slf4j
public class ManualTransactionManager {

  private static final ThreadLocal<SessionFactory> holder = new ThreadLocal<>();

  private final ModelSessionFactory sessionFactory;

  public ManualTransactionManager(ModelSessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  protected SessionFactory buildCurrentSessionFactory() {
    SessionFactory sessionFactoryHolder = holder.get();
    if (sessionFactoryHolder != null) {
      holder.remove();
    }
    holder.set(this.sessionFactory.real());
    return holder.get();
  }

  public TransactionStatus getTransaction() {
    SessionFactory sessionFactory = this.buildCurrentSessionFactory();
    int retryCount = 0;
    while (sessionFactory == null && ++retryCount <= 3) {
      try {
        Thread.sleep(TimeUnit.SECONDS.toMillis(5));
      } catch (InterruptedException e) {
        log.error(e.getMessage(), e);
      }
      sessionFactory = this.buildCurrentSessionFactory();
    }
    if (sessionFactory == null) {
      throw new ValidationException("Build SessionFactory Failure");
    }
    return TransactionStatus.builder().sessionFactory(sessionFactory).build();
  }

  public static SessionFactory getCurrentSessionFactory() {
    return holder.get();
  }
}
