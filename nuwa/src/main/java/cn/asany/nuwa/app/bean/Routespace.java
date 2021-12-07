package cn.asany.nuwa.app.bean;

import cn.asany.nuwa.template.bean.ApplicationTemplate;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
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
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "NUWA_ROUTESPACE")
@NamedEntityGraph(
    name = "Graph.Routespace.FetchApplicationTemplate",
    attributeNodes = {
      @NamedAttributeNode(
          value = "applicationTemplate",
          subgraph = "SubGraph.ApplicationTemplate.FetchDetails"),
    },
    subgraphs = {
      @NamedSubgraph(
          name = "SubGraph.ApplicationTemplate.FetchDetails",
          attributeNodes = {
            @NamedAttributeNode(
                value = "routes",
                subgraph = "SubGraph.ApplicationTemplateRoute.FetchComponent"),
          }),
      @NamedSubgraph(
          name = "SubGraph.ApplicationTemplateRoute.FetchComponent",
          attributeNodes = {
            @NamedAttributeNode(value = "parent"),
            @NamedAttributeNode(value = "component")
          })
    })
public class Routespace extends BaseBusEntity {

  public static Routespace DEFAULT_ROUTESPACE_WEB =
      Routespace.builder().id("web").name("PC Web").build();
  public static Routespace DEFAULT_ROUTESPACE_WAP =
      Routespace.builder().id("wap").name("Wap 网站").build();

  @Id
  @Column(name = "ID", length = 40)
  private String id;

  @Column(name = "NAME", length = 50)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @LazyToOne(LazyToOneOption.NO_PROXY)
  @JoinColumn(
      name = "APP_TEMPLATE_ID",
      foreignKey = @ForeignKey(name = "FK_ROUTESPACE_APP_TEMP_ID"))
  @ToString.Exclude
  private ApplicationTemplate applicationTemplate;

  @ManyToMany(fetch = FetchType.LAZY)
  @LazyCollection(LazyCollectionOption.EXTRA)
  @JoinTable(
      name = "NUWA_APPLICATION_ROUTESPACE",
      joinColumns = @JoinColumn(name = "ROUTESPACE_ID"),
      inverseJoinColumns = @JoinColumn(name = "APPLICATION_ID"),
      foreignKey = @ForeignKey(name = "FK_APPLICATION_ROUTESPACE_SPACEID"))
  @ToString.Exclude
  private List<Application> applications;

  public Routespace getDEFAULT_ROUTESPACE_WEB() {
    return DEFAULT_ROUTESPACE_WEB;
  }

  public static class RoutespaceBuilder {

    public RoutespaceBuilder applicationTemplate(Long id) {
      this.applicationTemplate = ApplicationTemplate.builder().id(id).build();
      return this;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Routespace that = (Routespace) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
