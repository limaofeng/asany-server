package cn.asany.cms.article.graphql;

import cn.asany.cms.article.domain.Comment;
import cn.asany.cms.article.domain.enums.CommentTargetType;
import cn.asany.cms.article.graphql.input.CommentInput;
import cn.asany.cms.article.service.CommentService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentGraphQLMutationResolver implements GraphQLMutationResolver {

  @Autowired private CommentService commentService;

  public Comment createComment(CommentTargetType targetType, String targetId, CommentInput input) {
    return this.commentService.addComment(targetType, targetId, input);
  }

  public Comment updateComment(Long id, Boolean merge, CommentInput input) {
    return this.commentService.updateComment(id, merge, input);
  }

  public Boolean removeComment(Long id) {
    return this.commentService.removeComment(id);
  }
}
