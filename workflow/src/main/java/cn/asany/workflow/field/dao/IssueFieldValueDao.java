package cn.asany.workflow.field.dao;

import java.util.List;
import net.whir.hos.issue.field.bean.IssueFieldValue;
import net.whir.hos.issue.main.bean.Issue;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-05-24 17:52
 */
@Repository
public interface IssueFieldValueDao extends JpaRepository<IssueFieldValue, Long> {
  List<IssueFieldValue> findByIssue(Issue issue);
}
