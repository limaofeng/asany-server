package cn.asany.pm.field.dao;

import cn.asany.pm.field.bean.FieldConfigurationItem;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueFieldConfigurationItemDao
    extends AnyJpaRepository<FieldConfigurationItem, Long> {}
