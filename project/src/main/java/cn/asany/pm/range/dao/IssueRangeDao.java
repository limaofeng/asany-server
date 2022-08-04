package cn.asany.pm.range.dao;

import cn.asany.pm.range.bean.IssueRange;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface IssueRangeDao extends JpaRepository<IssueRange, Long> {}
