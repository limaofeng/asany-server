package cn.asany.cms.circle.dao;

import cn.asany.cms.circle.domain.CircleMember;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/** Created by admin on 2017/7/17. */
@Repository
public interface CircleMemberDao extends AnyJpaRepository<CircleMember, Long> {}
