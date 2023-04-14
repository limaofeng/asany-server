package cn.asany.message.define.dao;

import cn.asany.message.define.domain.MessageTemplate;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 消息模版 DAO
 *
 * @author limaofeng
 */
@Repository
public interface MessageTemplateDao extends JpaRepository<MessageTemplate, String> {}
