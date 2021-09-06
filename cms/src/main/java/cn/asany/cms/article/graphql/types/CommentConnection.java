package cn.asany.cms.article.graphql.types;

import cn.asany.cms.article.bean.Comment;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommentConnection extends BaseConnection<CommentConnection.CommentEdge> {

  private List<CommentEdge> edges;

  @Data
  public static class CommentEdge implements Edge<Comment> {
    private String cursor;
    private Comment node;
  }
}
