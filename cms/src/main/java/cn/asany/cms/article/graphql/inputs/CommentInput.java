package cn.asany.cms.article.graphql.inputs;

import cn.asany.cms.article.bean.Comment;
import cn.asany.cms.article.bean.enums.CommentTargetType;
import java.util.List;

public class CommentInput {

  private Comment.CommentBuilder builder = Comment.builder();

  public void setTitle(String title) {
    builder.title(title);
  }

  public void setContent(String content) {
    builder.content(content);
  }

  public void setForComment(long forComment) {
    builder.forComment(Comment.builder().id(forComment).build());
  }

  public void setReplyComments(List<Comment> replyComments) {
    builder.replyComments(replyComments);
  }

  public void setUid(String uid) {
    builder.uid(uid);
  }

  public void setTargetType(CommentTargetType targetType) {
    builder.targetType(targetType);
  }

  public void setTargetId(String targetId) {
    builder.targetId(targetId);
  }

  public Comment build() {
    return builder.build();
  }
}
