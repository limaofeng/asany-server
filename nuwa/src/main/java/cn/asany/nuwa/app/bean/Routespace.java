package cn.asany.nuwa.app.bean;

import cn.asany.nuwa.template.bean.ApplicationTemplate;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 区分不同的端
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(exclude = {"applications", "applicationTemplate"})
@Entity
@Table(name = "NUWA_ROUTESPACE")
@NamedEntityGraph(
    name = "Graph.Routespace.FetchApplicationTemplateRoute",
    attributeNodes = {
      @NamedAttributeNode(
          value = "applicationTemplate",
          subgraph = "SubGraph.ApplicationTemplate.FetchRoute"),
    },
    subgraphs = {
      @NamedSubgraph(
          name = "SubGraph.ApplicationTemplate.FetchRoute",
          attributeNodes = {
            @NamedAttributeNode(
                value = "routes",
                subgraph = "SubGraph.ApplicationTemplateRoute.FetchComponent")
          }),
      @NamedSubgraph(
          name = "SubGraph.ApplicationTemplateRoute.FetchComponent",
          attributeNodes = {
            @NamedAttributeNode(value = "parent"),
            @NamedAttributeNode(value = "component")
          })
    })
public class Routespace extends BaseBusEntity {
  @Id
  @Column(name = "ID", length = 40)
  private String id;

  @Column(name = "NAME", length = 50)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @LazyToOne(LazyToOneOption.NO_PROXY)
  @JoinColumn(
      name = "APP_TEMPLATE_ID",
      foreignKey = @ForeignKey(name = "FK_ROUTESPACE_APP_TEMP_ID"),
      nullable = false)
  private ApplicationTemplate applicationTemplate;

  @ManyToMany(fetch = FetchType.LAZY)
  @LazyCollection(LazyCollectionOption.EXTRA)
  @JoinTable(
      name = "NUWA_APPLICATION_ROUTESPACE",
      joinColumns = @JoinColumn(name = "ROUTESPACE_ID"),
      inverseJoinColumns = @JoinColumn(name = "APPLICATION_ID"),
      foreignKey = @ForeignKey(name = "FK_APPLICATION_ROUTESPACE_SPACEID"))
  private List<Application> applications;

  public static class RoutespaceBuilder {

    public RoutespaceBuilder applicationTemplate(Long id) {
      this.applicationTemplate = ApplicationTemplate.builder().id(id).build();
      return this;
    }
  }
}
