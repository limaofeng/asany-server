package cn.asany.security.oauth.dao;


import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationDao extends JpaRepository<Application, String> {
}
