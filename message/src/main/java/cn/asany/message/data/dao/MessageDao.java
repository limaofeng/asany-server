package cn.asany.message.data.dao;

import cn.asany.message.data.domain.Message;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 消息数据访问层
 *
 * @author limaofeng
 */
@Repository
public interface MessageDao extends JpaRepository<Message, Long> {}
