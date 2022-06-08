package cn.asany.cms.article.graphql.input;

import lombok.Data;

@Data
public class AcceptArticleChannel {
  /** 是否包含子栏目 */
  private Boolean subColumns = Boolean.FALSE;
  /** 栏目ID */
  private String id;
}
