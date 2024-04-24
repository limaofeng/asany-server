package cn.asany.pm.issue.core.dao;

import cn.asany.pm.issue.core.domain.Comment;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Repository("issue.CommentDao")
public interface CommentDao extends AnyJpaRepository<Comment, Long> {}
