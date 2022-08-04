package cn.asany.pm.field.dao;

import cn.asany.pm.field.bean.FieldConfigurationItem;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueFieldConfigurationItemDao
    extends JpaRepository<FieldConfigurationItem, Long> {}
