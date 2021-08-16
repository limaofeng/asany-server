package cn.asany.nuwa.template.bean;

import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "NUWA_APPLICATION_TEMPLATE")
@NamedEntityGraph(
    name = "Graph.ApplicationTemplate.FetchRoute",
    attributeNodes = {
      @NamedAttributeNode(value = "routes"),
    })
public class ApplicationTemplate extends BaseBusEntity {

  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 简介 */
  @Column(name = "DESCRIPTION")
  private String description;
  /** 路由 */
  @OneToMany(
      mappedBy = "application",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
      fetch = FetchType.LAZY)
  private List<ApplicationTemplateRoute> routes;
}
