package cn.asany.sms.dao;

import cn.asany.sms.domain.Template;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateDao extends JpaRepository<Template, Long> {}
