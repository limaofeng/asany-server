package cn.asany.security.core.dao;

import cn.asany.security.core.domain.RoleSpace;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author liumeng @Description: (这里用一句话描述这个类的作用)
 * @date 15:40 2020-04-23
 */
@Repository
public interface RoleSpaceDao extends JpaRepository<RoleSpace, String> {}
