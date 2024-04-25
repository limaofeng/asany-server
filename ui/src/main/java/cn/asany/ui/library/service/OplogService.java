/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.ui.library.service;

import cn.asany.ui.library.OplogDataCollector;
import cn.asany.ui.library.dao.OplogDao;
import cn.asany.ui.library.domain.Oplog;
import cn.asany.ui.library.domain.enums.Operation;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.dao.hibernate.util.HibernateUtils;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.spring.SpringBeanUtils;
import net.asany.jfantasy.framework.util.common.ClassUtil;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.framework.util.concurrent.LinkedQueue;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 日志服务
 *
 * @author limaofeng
 */
@Slf4j
@Service
public class OplogService implements InitializingBean {
  private final LinkedQueue<Oplog> queue = new LinkedQueue<>();

  private final OplogDao oplogDao;
  private final SchedulingTaskExecutor executor;

  public OplogService(
      OplogDao oplogDao, @Qualifier("taskExecutor") SchedulingTaskExecutor executor) {
    this.oplogDao = oplogDao;
    this.executor = executor;
  }

  @Override
  public void afterPropertiesSet() {
    executor.execute(this::execute);
  }

  public void execute() {
    List<Oplog> cache = new ArrayList<>();
    OplogService oplogService = SpringBeanUtils.getBeanByType(OplogService.class);
    do {
      Oplog oplog;
      try {
        oplog = queue.poll(500, TimeUnit.MILLISECONDS);
      } catch (InterruptedException e) {
        log.error(e.getMessage());
        break;
      }
      if (oplog != null) {
        cache.add(oplog);
      } else if (!cache.isEmpty()) {
        oplogService.save(cache);
        cache.clear();
      }
    } while (true);
  }

  @Transactional(rollbackOn = Exception.class)
  public void save(List<Oplog> logs) {
    this.oplogDao.saveAllInBatch(logs);
  }

  public Oplog buildOplog(Operation operation, Object entity) {
    OplogDataCollector collector =
        entity instanceof OplogDataCollector
            ? (OplogDataCollector) entity
            : OplogDataCollector.EMPTY_OPLOG_DATA_COLLECTOR;
    Class<?> entityClass =
        ObjectUtil.defaultValue(
            collector.getEntityClass(), () -> ClassUtil.getRealClass(entity.getClass()));
    String entityName =
        ObjectUtil.defaultValue(
            collector.getEntityName(), () -> HibernateUtils.getEntityName(entityClass));
    String tableName =
        ObjectUtil.defaultValue(
            collector.getTableName(), () -> HibernateUtils.getTableName(entityClass));
    String primarykeyName =
        ObjectUtil.defaultValue(
            collector.getPrimarykeyName(), () -> HibernateUtils.getIdName(entityClass));
    Object primarykey =
        ObjectUtil.defaultValue(
            collector.getPrimarykey(), () -> HibernateUtils.getIdValue(entityClass, entity));
    List<String> owners = ObjectUtil.defaultValue(collector.getOwners(), Collections.emptyList());
    return Oplog.builder()
        .operation(operation)
        .clazz(entityClass.getName())
        .entityName(entityName)
        .tableName(tableName)
        .primarykeyName(primarykeyName)
        .primarykeyValue(primarykey.toString())
        .owners(owners)
        .build();
  }

  @Async
  public void log(Operation operation, Object entity) {
    try {
      this.queue.put(buildOplog(operation, entity));
    } catch (InterruptedException e) {
      log.error(e.getMessage());
    }
  }

  public void log(Operation operation, List<Object> objects) {
    try {
      for (Object obj : objects) {
        this.queue.put(buildOplog(operation, obj));
      }
    } catch (InterruptedException e) {
      log.error(e.getMessage());
    }
  }

  public List<Oplog> oplogs(PropertyFilter filter) {
    return this.oplogDao.findAll(filter, Sort.by("createdAt"));
  }
}
