package cn.asany.shanhai.core.domain;

import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(of = "id")
@Entity
@Table(name = "SH_MODEL_FEATURE")
public class ModelFeature extends BaseBusEntity {

  @Id
  @Column(name = "ID", length = 16)
  private String id;

  @Column(name = "NAME", length = 20)
  private String name;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "SH_MODEL_FEATURE_RELATION",
      joinColumns = {@JoinColumn(name = "FEATURE_ID")},
      inverseJoinColumns = {@JoinColumn(name = "MODEL_ID")},
      foreignKey = @ForeignKey(name = "FK_MODEL_FEATURE_RELATION_FID"))
  private List<Model> models;
}
