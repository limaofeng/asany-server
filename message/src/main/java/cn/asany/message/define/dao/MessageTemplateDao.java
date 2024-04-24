package cn.asany.message.define.dao;

import cn.asany.message.define.domain.MessageTemplate;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 消息模版 DAO
 *
 * @author limaofeng
 */
@Repository
public interface MessageTemplateDao extends AnyJpaRepository<MessageTemplate, Long> {}
