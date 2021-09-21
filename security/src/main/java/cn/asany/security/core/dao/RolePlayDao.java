package cn.asany.security.core.dao;

import cn.asany.security.core.bean.RolePlay;
import java.util.List;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author liumeng @Description: (这里用一句话描述这个类的作用)
 * @date 15:40 2020-04-23
 */
@Repository
public interface RolePlayDao extends JpaRepository<RolePlay, String> {
  /**
   * 获取 RolePlay
   *
   * @param roleId 角色 ID
   * @param player player
   * @return List<RolePlay>
   */
  List<RolePlay> findByRoleIdAndPlayer(Long roleId, String player);
}
