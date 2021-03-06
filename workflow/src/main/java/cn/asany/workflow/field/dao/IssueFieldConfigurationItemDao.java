package cn.asany.workflow.field.dao;

import net.whir.hos.issue.field.bean.IssueFieldConfigurationItem;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueFieldConfigurationItemDao
    extends JpaRepository<IssueFieldConfigurationItem, Long> {}
