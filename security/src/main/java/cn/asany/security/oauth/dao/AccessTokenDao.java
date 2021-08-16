package cn.asany.security.oauth.dao;

import cn.asany.security.oauth.bean.AccessToken;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessTokenDao extends JpaRepository<AccessToken, Long> {}
