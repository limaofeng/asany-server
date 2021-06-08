package cn.asany.security.oauth.dao;


import cn.asany.security.oauth.bean.OAuthApplication;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthApplicationDao extends JpaRepository<OAuthApplication, String> {
}
