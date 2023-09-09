package cn.asany.cms.article.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章相关组件
 *
 * @author limaofeng
 */
@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ArticleComponents implements Serializable {
  /** 展示组件 */
  @Column(name = "VIEW_COMPONENT", length = 250)
  private String view;

  /** 新增组件 */
  @Column(name = "ADDED_COMPONENT", length = 250)
  private String added;

  /** 编辑组件 */
  @Column(name = "EDIT_COMPONENT", length = 250)
  private String edit;

  /** 列表组件 */
  @Column(name = "LIST_COMPONENT", length = 250)
  private String list;
}
