package cn.asany.cms.article.dao;

import cn.asany.cms.article.domain.Comment;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentDao extends AnyJpaRepository<Comment, Long> {}
