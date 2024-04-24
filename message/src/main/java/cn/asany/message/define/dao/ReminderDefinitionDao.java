package cn.asany.message.define.dao;

import cn.asany.message.define.domain.ReminderDefinition;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 消息提醒定义 DAO
 *
 * @author limaofeng
 */
@Repository
public interface ReminderDefinitionDao extends AnyJpaRepository<ReminderDefinition, String> {}
