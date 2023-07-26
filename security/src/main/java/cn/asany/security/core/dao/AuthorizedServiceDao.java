package cn.asany.security.core.dao;

import cn.asany.security.core.domain.AuthorizedService;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 可授权的服务
 *
 * @author limaofeng
 */
@Repository
public interface AuthorizedServiceDao extends JpaRepository<AuthorizedService, String> {}
