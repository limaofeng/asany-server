package cn.asany.cms.article.graphql.types;

import cn.asany.cms.article.bean.Comment;
import java.util.List;
import lombok.Data;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

@Data
public class CommentConnection extends BaseConnection<CommentConnection.CommentEdge> {

  private List<CommentEdge> edges;

  @Data
  public static class CommentEdge implements Edge<Comment> {
    private String cursor;
    private Comment node;
  }
}
