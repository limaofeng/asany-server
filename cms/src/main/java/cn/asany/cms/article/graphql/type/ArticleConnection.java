package cn.asany.cms.article.graphql.type;

import cn.asany.cms.article.domain.Article;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

/**
 * 文章分页对象
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleConnection extends BaseConnection<ArticleConnection.ArticleEdge, Article> {
  private List<ArticleEdge> edges;

  @Data
  public static class ArticleEdge implements Edge<Article> {
    private String cursor;
    private Article node;
  }
}
