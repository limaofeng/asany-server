package cn.asany.cms.article.graphql.resolver;

import cn.asany.cms.article.domain.Comment;
import cn.asany.cms.article.graphql.enums.CommentStarType;
import cn.asany.cms.article.graphql.input.CommentFilter;
import cn.asany.cms.article.graphql.type.Starrable;
import cn.asany.cms.article.service.CommentService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.List;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 * @version V1.0
 * @date 2019-08-15 09:53
 */
@Component
public class CommentGraphQLResolver implements GraphQLResolver<Comment> {

  @Autowired private CommentService commentService;

  public String user(Comment comment) {
    return comment.getUid();
  }

  public List<Comment> comments(Comment comment, CommentFilter filter) {
    PropertyFilterBuilder builder =
        ObjectUtil.defaultValue(filter, new CommentFilter()).getBuilder();
    builder.startsWith("path", comment.getPath());
    builder.notEqual("id", comment.getId());
    return commentService.findAll(builder.build());
  }

  public Starrable starrable(Comment comment, CommentStarType starType) {
    //        .id(comment.getId().toString() + "/" + starType.getValue())
    //                .galaxy(comment.getId().toString())
    //                .starType(starType.getValue())
    return Starrable.builder().build();
  }
}
