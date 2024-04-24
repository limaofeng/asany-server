package cn.asany.security.core.dao;

import cn.asany.security.core.domain.AuthorizedService;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 可授权的服务
 *
 * @author limaofeng
 */
@Repository
public interface AuthorizedServiceDao extends AnyJpaRepository<AuthorizedService, String> {}
