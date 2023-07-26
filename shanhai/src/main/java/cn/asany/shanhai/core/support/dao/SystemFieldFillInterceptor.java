package cn.asany.shanhai.core.support.dao;

import java.io.Serializable;
import java.util.Arrays;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.SoftDeletableBaseBusEntity;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.ognl.OgnlUtil;

/**
 * 填充系统默认字段
 *
 * @author limaofeng
 */
public class SystemFieldFillInterceptor extends EmptyInterceptor {

  private static final OgnlUtil OGNL_UTIL = OgnlUtil.getInstance();

  @Override
  public boolean onFlushDirty(
      Object entity,
      Serializable id,
      Object[] currentState,
      Object[] previousState,
      String[] propertyNames,
      Type[] types) {
    if (hasBaseFields(propertyNames)) {
      LoginUser user = SpringSecurityUtils.getCurrentUser();
      Long updatedBy = getFieldValue(entity, BaseBusEntity.FIELD_UPDATED_BY);
      Object updatedAt = getFieldValue(entity, BaseBusEntity.FIELD_UPDATED_BY);
      if (updatedBy == null && user != null) {
        updatedBy = user.getUid();
      }
      if (updatedAt == null) {
        updatedAt = DateUtil.now().clone();
      }
      int count = 0;
      for (int i = 0; i < propertyNames.length; i++) {
        if (BaseBusEntity.FIELD_UPDATED_BY.equals(propertyNames[i])) {
          currentState[i] = updatedBy;
          count++;
        } else if (BaseBusEntity.FIELD_UPDATED_AT.equals(propertyNames[i])) {
          currentState[i] = updatedAt;
          count++;
        }
        if (count >= 2) {
          return true;
        }
      }
    }
    return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
  }

  private <T> T getFieldValue(Object entity, String filedName) {
    return OGNL_UTIL.getValue(filedName, entity);
  }

  private boolean hasBaseFields(String[] propertyNames) {
    return Arrays.stream(propertyNames)
        .anyMatch(item -> ObjectUtil.exists(BaseBusEntity.ALL_FIELD, item));
  }

  @Override
  public boolean onSave(
      Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
    if (hasBaseFields(propertyNames)) {
      LoginUser user = SpringSecurityUtils.getCurrentUser();

      Long createdBy = getFieldValue(entity, BaseBusEntity.FIELD_CREATED_BY);
      Object createdAt = getFieldValue(entity, BaseBusEntity.FIELD_CREATED_AT);
      if (createdBy == null && user != null) {
        createdBy = user.getUid();
      }
      if (createdAt == null) {
        createdAt = DateUtil.now().clone();
      }
      int count = 0;
      int maxCount = 4;
      if (entity instanceof SoftDeletableBaseBusEntity) {
        maxCount++;
      }
      for (int i = 0; i < propertyNames.length; i++) {
        if (BaseBusEntity.FIELD_CREATED_BY.equals(propertyNames[i])
            || BaseBusEntity.FIELD_UPDATED_BY.equals(propertyNames[i])) {
          state[i] = createdBy;
          count++;
        } else if (BaseBusEntity.FIELD_CREATED_AT.equals(propertyNames[i])
            || BaseBusEntity.FIELD_UPDATED_AT.equals(propertyNames[i])) {
          state[i] = createdAt;
          count++;
        }
        if (count >= maxCount) {
          return true;
        }
      }
    }
    return super.onSave(entity, id, state, propertyNames, types);
  }
}
