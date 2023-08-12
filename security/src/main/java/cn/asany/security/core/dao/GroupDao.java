package cn.asany.security.core.dao;

import cn.asany.security.core.domain.Group;
import cn.asany.security.core.domain.GroupPrimaryKey;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户组 DAO
 *
 * @author limaofeng
 */
@Repository
public interface GroupDao extends JpaRepository<Group, GroupPrimaryKey> {
}
