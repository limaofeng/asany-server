package cn.asany.cms.article.graphql.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcceptArticleCategory {
  /** 是否包含子栏目 */
  private Boolean subColumns = Boolean.FALSE;
  /** 栏目ID */
  private String id;
}
