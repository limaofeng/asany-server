package cn.asany.storage.data.dao;

import cn.asany.storage.data.domain.Space;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceDao extends JpaRepository<Space, String> {}
