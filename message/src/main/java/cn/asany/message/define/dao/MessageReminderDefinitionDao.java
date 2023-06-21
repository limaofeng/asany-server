package cn.asany.message.define.dao;

import cn.asany.message.define.domain.MessageReminderDefinition;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 消息提醒定义 DAO
 *
 * @author limaofeng
 */
@Repository
public interface MessageReminderDefinitionDao
    extends JpaRepository<MessageReminderDefinition, String> {}
