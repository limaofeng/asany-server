package cn.asany.workflow.field.dao;

import net.whir.hos.issue.field.bean.IssueFieldConfigurationScheme;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-05-24 17:26
 */
@Repository
public interface IssueFieldConfigurationSchemeDao
    extends JpaRepository<IssueFieldConfigurationScheme, Long> {}
