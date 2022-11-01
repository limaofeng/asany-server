package cn.asany.shanhai.view.domain;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.view.domain.enums.ModelViewType;
import cn.asany.ui.resources.domain.Component;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.jfantasy.framework.dao.BaseBusEntity;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(
    name = "SH_MODEL_VIEW",
    uniqueConstraints =
        @UniqueConstraint(
            name = "UK_MODEL_VIEW_NAME",
            columnNames = {"NAME", "MODEL_ID"}))
public class ModelView extends BaseBusEntity {
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 名称 */
  @Column(name = "NAME", length = 100)
  private String name;
  /** 视图类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE")
  private ModelViewType type;
  /** 是否为默认视图 */
  @Builder.Default
  @Column(name = "IS_DEFAULT", updatable = false)
  private Boolean defaultView = Boolean.FALSE;
  /**
   * 组件<br>
   * 可以为一级菜单设置一个展示组件
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      nullable = false,
      name = "COMPONENT_ID",
      foreignKey = @ForeignKey(name = "FK_MODEL_VIEW_COMPONENT"))
  @ToString.Exclude
  private Component component;
  /** 实体 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "MODEL_ID",
      foreignKey = @ForeignKey(name = "FK_MODEL_VIEW_MODEL_ID"),
      updatable = false,
      nullable = false)
  @LazyToOne(LazyToOneOption.NO_PROXY)
  @ToString.Exclude
  private Model model;
}
