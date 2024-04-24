package cn.asany.nuwa.template.dao;

import cn.asany.nuwa.template.domain.ApplicationTemplate;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationTemplateDao extends AnyJpaRepository<ApplicationTemplate, Long> {}
