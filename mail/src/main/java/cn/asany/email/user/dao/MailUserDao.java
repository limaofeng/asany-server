package cn.asany.email.user.dao;

import cn.asany.email.user.domain.MailUser;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 邮件用户
 *
 * @author limaofeng
 */
@Repository("JamesUserDao")
public interface MailUserDao extends AnyJpaRepository<MailUser, String> {}
