package cn.asany.workflow.field.dao;

import java.util.List;
import cn.asany.pm.field.bean.IssueFieldValue;
import cn.asany.pm.issue.bean.Issue;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface IssueFieldValueDao extends AnyJpaRepository<IssueFieldValue, Long> {
  List<IssueFieldValue> findByIssue(Issue issue);
}
