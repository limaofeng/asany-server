package cn.asany.shanhai.gateway.domain;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.ModelField;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.MetaValue;
import org.jfantasy.framework.dao.BaseBusEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "SH_MODEL_GROUP_ITEM")
public class ModelGroupItem extends BaseBusEntity {
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 引用资源 */
  @Any(
      metaColumn =
          @Column(name = "RESOURCE_TYPE", length = 10, insertable = false, updatable = false),
      fetch = FetchType.LAZY)
  @AnyMetaDef(
      idType = "long",
      metaType = "string",
      metaValues = {
        @MetaValue(targetEntity = ModelField.class, value = "ENDPOINT"),
        @MetaValue(targetEntity = Model.class, value = "MODEL")
      })
  @JoinColumn(name = "RESOURCE_ID", insertable = false, updatable = false)
  private ModelGroupResource resource;

  @Column(name = "RESOURCE_ID")
  private Long resourceId;

  @Column(name = "RESOURCE_TYPE", length = 10)
  private String resourceType;

  /** 排序 */
  @Column(name = "SORT")
  private Integer sort;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "GROUP_ID",
      foreignKey = @ForeignKey(name = "FK_MODEL_GROUP_ITEM_GID"),
      nullable = false)
  private ModelGroup group;
}
