package cn.asany.security.core.dao;

import cn.asany.security.core.bean.RoleType;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: guoyong
 * @description: 角色分类Dao
 * @create: 2020/6/9 15:10
 */
@Repository
public interface RoleTypeDao extends JpaRepository<RoleType, String> {
}
