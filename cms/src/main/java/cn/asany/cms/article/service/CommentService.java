package cn.asany.cms.article.service;

import cn.asany.cms.article.bean.Comment;
import cn.asany.cms.article.bean.enums.CommentStatus;
import cn.asany.cms.article.bean.enums.CommentTargetType;
import cn.asany.cms.article.dao.CommentDao;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentService {

  private static final String PATH_SEPARATOR = "/";

  @Autowired private CommentDao commentDao;

  public Comment addComment(CommentTargetType targetType, String targetId, Comment comment) {
    comment.setTargetType(targetType);
    comment.setTargetId(targetId);
    comment.setStatus(CommentStatus.pending);
    this.commentDao.save(comment);
    if (comment.getForComment() != null) {
      Comment forComment = this.commentDao.getOne(comment.getForComment().getId());
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

  public Pager<Comment> findPager(Pager<Comment> pager, List<PropertyFilter> filters) {

    return this.commentDao.findPager(pager, filters);
  }

  public Comment get(Long id) {
    return this.commentDao.getOne(id);
  }

  public List<Comment> findAll(List<PropertyFilter> filters) {
    OrderBy orderBy = new OrderBy("createdAt", OrderBy.Direction.ASC);
    return this.commentDao.findAll(filters, orderBy.toSort());
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
