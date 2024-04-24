package cn.asany.message.data.dao;

import cn.asany.message.data.domain.Message;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 消息数据访问层
 *
 * @author limaofeng
 */
@Repository
public interface MessageDao extends AnyJpaRepository<Message, Long> {}
