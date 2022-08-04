package cn.asany.workflow.field.dao;

import java.util.List;
import cn.asany.pm.field.bean.IssueFieldValue;
import cn.asany.pm.issue.bean.Issue;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface IssueFieldValueDao extends JpaRepository<IssueFieldValue, Long> {
  List<IssueFieldValue> findByIssue(Issue issue);
}
