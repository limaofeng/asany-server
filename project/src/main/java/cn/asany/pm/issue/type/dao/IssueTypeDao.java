package cn.asany.pm.issue.type.dao;

import cn.asany.pm.issue.type.domain.IssueType;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueTypeDao extends JpaRepository<IssueType, Long> {}
