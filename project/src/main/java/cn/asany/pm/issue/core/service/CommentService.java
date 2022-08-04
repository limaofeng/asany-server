package cn.asany.pm.issue.core.service;

import cn.asany.pm.issue.core.dao.CommentDao;
import cn.asany.pm.issue.core.domain.Comment;
import cn.asany.pm.issue.core.domain.Issue;
import java.util.Date;
import org.springframework.stereotype.Service;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Service("issue.CommentService")
public class CommentService {
  private final CommentDao commentDao;

  public CommentService(CommentDao commentDao) {
    this.commentDao = commentDao;
  }

  public Comment save(Long id, Comment comment) {
    comment.setIssue(Issue.builder().id(id).build());
    comment.setContentDate(new Date());
    return commentDao.save(comment);
  }
}
