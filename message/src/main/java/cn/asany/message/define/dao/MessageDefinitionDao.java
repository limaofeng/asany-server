package cn.asany.message.define.dao;

import cn.asany.message.define.domain.MessageDefinition;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 消息定义 DAO
 *
 * @author limaofeng
 */
@Repository
public interface MessageDefinitionDao extends AnyJpaRepository<MessageDefinition, Long> {}
