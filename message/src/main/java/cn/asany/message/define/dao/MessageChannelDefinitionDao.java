package cn.asany.message.define.dao;

import cn.asany.message.define.domain.MessageChannelDefinition;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 消息发送者定义 DAO
 *
 * @author limaofeng
 */
@Repository
public interface MessageChannelDefinitionDao
    extends JpaRepository<MessageChannelDefinition, Long> {}
