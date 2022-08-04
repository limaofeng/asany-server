package cn.asany.pm.issue.core.dao;

import cn.asany.pm.issue.core.domain.Issue;
import java.util.List;
import java.util.Map;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface IssueDao extends JpaRepository<Issue, Long> {

  @Query(
      nativeQuery = true,
      value =
          "SELECT assignee ,count(id) as count FROM gd_issue WHERE status_id IN('2','3','4') GROUP BY  assignee")
  List<Map<String, Object>> assigneeCount();
}
