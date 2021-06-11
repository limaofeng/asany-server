package cn.asany.nuwa.template.dao;

import cn.asany.nuwa.template.bean.ApplicationTemplate;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationTemplateDao extends JpaRepository<ApplicationTemplate, Long> {
}
