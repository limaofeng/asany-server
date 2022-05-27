package cn.asany.shanhai.gateway.domain;

import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/** @author limaofeng */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@NamedEntityGraph(
    name = "Graph.ModelGroup.FetchChildren",
    attributeNodes = @NamedAttributeNode(value = "items"))
@Table(name = "SH_MODEL_GROUP")
public class ModelGroup extends BaseBusEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 名称 */
  @Column(name = "NAME", length = 100)
  private String name;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 500)
  private String description;
  /** 排序 */
  @Column(name = "SORT")
  private Integer sort;
  /** 分组明显 */
  @OneToMany(
      mappedBy = "group",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
      fetch = FetchType.LAZY)
  private List<ModelGroupItem> items;
}
