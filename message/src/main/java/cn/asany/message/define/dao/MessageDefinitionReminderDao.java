package cn.asany.message.define.dao;

import cn.asany.message.define.domain.MessageDefinitionReminder;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageDefinitionReminderDao
    extends JpaRepository<MessageDefinitionReminder, String> {}
