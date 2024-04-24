package cn.asany.pm.issue.type.dao;

import cn.asany.pm.issue.type.domain.IssueType;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueTypeDao extends AnyJpaRepository<IssueType, Long> {}
