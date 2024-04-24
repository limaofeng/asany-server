package cn.asany.security.oauth.dao;

import cn.asany.security.oauth.domain.AccessToken;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessTokenDao extends AnyJpaRepository<AccessToken, Long> {}
