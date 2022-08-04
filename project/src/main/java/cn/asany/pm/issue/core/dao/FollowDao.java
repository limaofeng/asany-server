package cn.asany.pm.issue.core.dao;

import cn.asany.pm.issue.core.domain.Follow;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface FollowDao extends JpaRepository<Follow, Long> {}
