package cn.asany.workflow.field.dao;

import cn.asany.pm.field.bean.IssueFieldConfigurationItem;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueFieldConfigurationItemDao
    extends AnyJpaRepository<IssueFieldConfigurationItem, Long> {}
