package cn.asany.message.define.dao;

import cn.asany.message.define.domain.MessageTemplate;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageTemplateDao extends JpaRepository<MessageTemplate, String> {}
