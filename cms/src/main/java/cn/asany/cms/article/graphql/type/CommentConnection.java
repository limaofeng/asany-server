package cn.asany.cms.article.graphql.type;

import cn.asany.cms.article.domain.Comment;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.graphql.Edge;
import net.asany.jfantasy.graphql.types.BaseConnection;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommentConnection extends BaseConnection<CommentConnection.CommentEdge, Comment> {

  private List<CommentEdge> edges;

  @Data
  public static class CommentEdge implements Edge<Comment> {
    private String cursor;
    private Comment node;
  }
}
