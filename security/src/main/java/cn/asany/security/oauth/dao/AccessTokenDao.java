package cn.asany.security.oauth.dao;

import cn.asany.security.oauth.bean.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessTokenDao extends JpaRepository<AccessToken, String> {
}
