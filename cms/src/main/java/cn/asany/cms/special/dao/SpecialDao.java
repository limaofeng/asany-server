package cn.asany.cms.special.dao;

import cn.asany.cms.special.domain.Special;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialDao extends JpaRepository<Special, String> {}
