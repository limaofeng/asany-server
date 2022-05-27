package cn.asany.cms.article.service;

import cn.asany.cms.article.dao.CommentDao;
import cn.asany.cms.article.domain.Comment;
import cn.asany.cms.article.domain.enums.CommentStatus;
import cn.asany.cms.article.domain.enums.CommentTargetType;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentService {

  private static final String PATH_SEPARATOR = "/";

  private final CommentDao commentDao;

  public CommentService(CommentDao commentDao) {
    this.commentDao = commentDao;
  }

  public Comment addComment(CommentTargetType targetType, String targetId, Comment comment) {
    comment.setTargetType(targetType);
    comment.setTargetId(targetId);
    comment.setStatus(CommentStatus.pending);
    this.commentDao.save(comment);
    if (comment.getForComment() != null) {
      Comment forComment = this.commentDao.getReferenceById(comment.getForComment().getId());
      comment.setPath(forComment.getPath() + comment.getId() + PATH_SEPARATOR);
    } else {
      comment.setPath(comment.getId() + PATH_SEPARATOR);
    }
    return this.commentDao.save(comment);
  }

  public Comment updateComment(Long id, Boolean merge, Comment comment) {
    comment.setId(id);
    return this.commentDao.update(comment, merge);
  }

  public Page<Comment> findPage(Pageable pageable, List<PropertyFilter> filters) {
    return this.commentDao.findPage(pageable, filters);
  }

  public Comment get(Long id) {
    return this.commentDao.getReferenceById(id);
  }

  public List<Comment> findAll(List<PropertyFilter> filters) {
    return this.commentDao.findAll(filters, Sort.by("createdAt").ascending());
  }

  public Boolean removeComment(Long id) {
    Optional<Comment> optionalComment = this.commentDao.findById(id);
    if (!optionalComment.isPresent()) {
      return false;
    }
    Comment comment = optionalComment.get();
    comment.setStatus(CommentStatus.removed);
    this.commentDao.save(comment);
    return true;
  }

  //    public boolean hasStarred(Comment comment, Long uid) {
  //        String targetId = comment.getId().toString();
  //        String type = "comment_likes";
  //        return
  // this.starDao.count(Example.of(Star.builder().starType(StarType.builder().id(type).build()).galaxy(targetId).stargazer(Employee.builder().id(uid).build()).build())) > 0;
  //    }

  //    public List<Employee> staremployees(Comment comment) {
  //        String targetId = comment.getId().toString();
  //        String type = "comment_likes";
  //        List <Star> stars =
  // this.starDao.findAll(Example.of(Star.builder().starType(StarType.builder().id(type).build()).galaxy(targetId).build()));
  //        return stars.stream().map(Star::getStargazer).collect(Collectors.toList());
  //    }
}
