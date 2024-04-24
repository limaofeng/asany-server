package cn.asany.pm.issue.attribute.dao;

import cn.asany.pm.issue.attribute.domain.Resolution;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Resolution Dao
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface ResolutionDao extends AnyJpaRepository<Resolution, Long> {}
