package cn.asany.organization.core.dao;

import cn.asany.organization.core.domain.TeamMember;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 团队成员
 *
 * @author limaofeng
 */
@Repository
public interface TeamMemberDao extends JpaRepository<TeamMember, Long> {
  /**
   * 删除成员 - 根据组织 ID
   *
   * @param orgId 组织 ID
   */
  @Modifying
  @Query(
      nativeQuery = true,
      value = "DELETE FROM org_team_member e WHERE e.organization_id = :orgId")
  void deleteByOrgId(@Param("orgId") Long orgId);
}
