package cn.asany.message.define.dao;

import cn.asany.message.define.domain.MessageChannelDefinition;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 消息发送者定义 DAO
 *
 * @author limaofeng
 */
@Repository
public interface MessageChannelDefinitionDao
    extends AnyJpaRepository<MessageChannelDefinition, Long> {}
