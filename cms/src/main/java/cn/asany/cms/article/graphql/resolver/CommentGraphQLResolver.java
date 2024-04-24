package cn.asany.cms.article.graphql.resolver;

import cn.asany.cms.article.domain.Comment;
import cn.asany.cms.article.graphql.enums.CommentStarType;
import cn.asany.cms.article.graphql.input.CommentWhereInput;
import cn.asany.cms.article.graphql.type.Starrable;
import cn.asany.cms.article.service.CommentService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.List;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Component
public class CommentGraphQLResolver implements GraphQLResolver<Comment> {

  @Autowired private CommentService commentService;

  public String user(Comment comment) {
    return comment.getUid();
  }

  public List<Comment> comments(Comment comment, CommentWhereInput where) {
    return commentService.findAll(
        ObjectUtil.defaultValue(where, new CommentWhereInput())
            .toFilter()
            .startsWith("path", comment.getPath())
            .notEqual("id", comment.getId()));
  }

  public Starrable starrable(Comment comment, CommentStarType starType) {
    //        .id(comment.getId().toString() + "/" + starType.getValue())
    //                .galaxy(comment.getId().toString())
    //                .starType(starType.getValue())
    return Starrable.builder().build();
  }
}
