package cn.asany.ui.library.service;

import cn.asany.ui.library.OplogDataCollector;
import cn.asany.ui.library.bean.Oplog;
import cn.asany.ui.library.bean.enums.Operation;
import cn.asany.ui.library.dao.OplogDao;
import org.jfantasy.framework.dao.hibernate.util.HibernateUtils;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class OplogService {

    private final OplogDao oplogDao;

    public OplogService(OplogDao oplogDao) {
        this.oplogDao = oplogDao;
    }

    @Async
    @Transactional
    public void log(Operation operation, Object entity) {
        OplogDataCollector collector = entity instanceof OplogDataCollector ? (OplogDataCollector) entity : OplogDataCollector.EMPTY_OPLOG_DATA_COLLECTOR;
        Class entityClass = ObjectUtil.defaultValue(collector.getEntityClass(), () -> ClassUtil.getRealClass(entity.getClass()));
        String entityName = ObjectUtil.defaultValue(collector.getEntityName(), () -> HibernateUtils.getEntityName(entityClass));
        String tableName = ObjectUtil.defaultValue(collector.getTableName(), () -> HibernateUtils.getTableName(entityClass));
        String primarykeyName = ObjectUtil.defaultValue(collector.getPrimarykeyName(), () -> HibernateUtils.getIdName(entityClass));
        Object primarykey = ObjectUtil.defaultValue(collector.getPrimarykey(), () -> HibernateUtils.getIdValue(entityClass, entity));
        List<String> owners = ObjectUtil.defaultValue(collector.getOwners(), Collections.emptyList());
        this.oplogDao.save(Oplog.builder()
            .operation(operation)
            .clazz(entityClass.getName())
            .entityName(entityName)
            .tableName(tableName)
            .primarykeyName(primarykeyName)
            .primarykeyValue(primarykey.toString())
            .owners(owners)
            .build());
    }

    public List<Oplog> oplogs(List<PropertyFilter> filters) {
        return this.oplogDao.findAll(filters, Sort.by("createdAt"));
    }
}
