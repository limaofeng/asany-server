package cn.asany.cms.article.dao;

import cn.asany.cms.article.domain.Comment;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentDao extends JpaRepository<Comment, Long> {}
