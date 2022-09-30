package cn.asany.shanhai.core.domain;

import cn.asany.shanhai.core.domain.enums.ModelRelationType;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/** 模型关系 记录模型与其他模型直接的关联关系 */
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "SH_MODEL_RELATION")
public class ModelRelation extends BaseBusEntity {
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 关系类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20)
  private ModelRelationType type;
  /** 关系类型 SUBJECTION INPUT / SLAVE */
  @Column(name = "RELATION", length = 100)
  private String relation;
  /** 当前实体 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "MODEL_ID",
      foreignKey = @ForeignKey(name = "FK_SH_MODEL_FIELD_MODEL_ID"),
      nullable = false)
  @ToString.Exclude
  private Model model;
  /** 关联实体 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "INVERSE",
      foreignKey = @ForeignKey(name = "FK_MODEL_RELATION_INVERSE"),
      nullable = false)
  @ToString.Exclude
  private Model inverse;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    ModelRelation that = (ModelRelation) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
