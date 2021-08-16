package cn.asany.security.core.dao;

import cn.asany.security.core.bean.PermissionType;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: guoyong
 * @description: 权限分类Dao
 * @create: 2020/6/9 15:07
 */
@Repository
public interface PermissionTypeDao extends JpaRepository<PermissionType, String> {}
