package cn.asany.shanhai.core.support.model;

import cn.asany.shanhai.core.support.dao.ManualTransactionManager;
import cn.asany.shanhai.core.support.dao.ModelRepository;
import java.util.List;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Page;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.spring.SpringBeanUtils;

public class DefaultCustomModelService implements CustomModelService {

  private final ModelRepository repository;

  public DefaultCustomModelService(ModelRepository repository) {
    this.repository = repository;
  }

  @Override
  public <T> T get(Long id) {
    return repository.findById(id);
  }

  @Override
  public <T> List<T> findAll(PropertyFilter filter, int offset, int limit, OrderBy sort) {
    return repository.findAll(filter, offset, limit, sort);
  }

  @Override
  public <T> Page<T> findPage(Page<T> page, PropertyFilter filter) {
    return repository.findPage(page, filter);
  }

  @Override
  public <T> T save(T entity) {
    ManualTransactionManager transactionManager =
        SpringBeanUtils.getBeanByType(ManualTransactionManager.class);
    //        transactionManager.bindSession();
    //        TransactionStatus transaction = transactionManager.getTransaction();
    try {
      repository.save(entity);
      //            transaction.commit();
      return entity;
    } catch (Exception e) {
      //            transaction.rollback();
      throw e;
    } finally {
      //            transactionManager.unbindSession();
    }
  }

  @Override
  public <T> T update(Long id, T entity, boolean merge) {
    return repository.update(id, entity, merge);
  }

  @Override
  public <T> T delete(Long id) {
    return repository.delete(id);
  }

  @Override
  public <T> List<T> deleteMany(PropertyFilter filter) {
    List<T> deletes = repository.findAll(filter);
    for (T t : deletes) {
      repository.delete(t);
    }
    return deletes;
  }
}
