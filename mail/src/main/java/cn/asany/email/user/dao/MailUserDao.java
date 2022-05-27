package cn.asany.email.user.dao;

import cn.asany.email.user.domain.MailUser;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 邮件用户
 *
 * @author limaofeng
 */
@Repository("JamesUserDao")
public interface MailUserDao extends JpaRepository<MailUser, String> {}
