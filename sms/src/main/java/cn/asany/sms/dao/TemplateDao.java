package cn.asany.sms.dao;

import cn.asany.sms.domain.Template;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateDao extends AnyJpaRepository<Template, Long> {}
