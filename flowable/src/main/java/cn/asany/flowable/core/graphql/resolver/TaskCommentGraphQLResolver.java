package cn.asany.flowable.core.graphql.resolver;

import graphql.kickstart.tools.GraphQLResolver;
import org.flowable.engine.task.Comment;
import org.springframework.stereotype.Component;

/**
 * 任务评论
 * @author limaofeng
 */
@Component
public class TaskCommentGraphQLResolver implements GraphQLResolver<Comment> {

  public String content(Comment comment) {
    return comment.getFullMessage();
  }
  
  public String author(Comment comment) {
    return comment.getUserId();
  }
  
}
