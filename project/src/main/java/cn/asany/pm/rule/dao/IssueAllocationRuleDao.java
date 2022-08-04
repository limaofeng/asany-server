package cn.asany.pm.rule.dao;

import cn.asany.pm.rule.bean.IssueAllocationRule;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface IssueAllocationRuleDao extends JpaRepository<IssueAllocationRule, Long> {

  // 向上排序
  @Modifying
  @Query(
      value =
          "UPDATE gd_issue_allocation_rule t  SET t.priority=t.priority+1  WHERE    t.priority<?1 and t.priority>=?2 ",
      nativeQuery = true)
  Integer rise(Long old, Long now);
  // 向下排序
  @Modifying
  @Query(
      value =
          "UPDATE gd_issue_allocation_rule t  SET t.priority=t.priority-1  WHERE    t.priority>?1 and t.priority<=?2 ",
      nativeQuery = true)
  Integer decline(Long old, Long now);

  // 重新排序
  @Modifying
  @Query(
      value =
          "UPDATE gd_issue_allocation_rule t  SET t.priority=t.priority-1  WHERE    t.priority>?1",
      nativeQuery = true)
  void resetSort(Long priority);
}
