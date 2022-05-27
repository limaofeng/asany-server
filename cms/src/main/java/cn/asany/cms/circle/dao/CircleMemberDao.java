package cn.asany.cms.circle.dao;

import cn.asany.cms.circle.domain.CircleMember;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/** Created by admin on 2017/7/17. */
@Repository
public interface CircleMemberDao extends JpaRepository<CircleMember, Long> {}
