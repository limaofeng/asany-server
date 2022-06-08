package cn.asany.cms.article.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Components {
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
