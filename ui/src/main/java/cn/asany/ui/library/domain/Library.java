package cn.asany.ui.library.domain;

import cn.asany.base.common.Ownership;
import cn.asany.organization.core.domain.Organization;
import cn.asany.security.core.domain.User;
import cn.asany.ui.library.OplogDataCollector;
import cn.asany.ui.library.dao.listener.OplogListener;
import cn.asany.ui.library.domain.enums.LibraryType;
import java.util.Set;
import jakarta.persistence.*;
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
@EqualsAndHashCode(callSuper = false)
@NamedEntityGraph(
    name = "Graph.Library.FetchIcon",
    attributeNodes = {
      @NamedAttributeNode(value = "items", subgraph = "SubGraph.LibraryItem.FetchIconResource")
    },
    subgraphs = {
      @NamedSubgraph(
          name = "SubGraph.LibraryItem.FetchIconResource",
          attributeNodes = {
            @NamedAttributeNode(value = "tags"),
            @NamedAttributeNode(value = "icon")
          }),
    })
@NamedEntityGraph(
    name = "Graph.Library.FetchComponent",
    attributeNodes = {
      @NamedAttributeNode(value = "items", subgraph = "SubGraph.LibraryItem.FetchComponentResource")
    },
    subgraphs = {
      @NamedSubgraph(
          name = "SubGraph.LibraryItem.FetchComponentResource",
          attributeNodes = {
            @NamedAttributeNode(value = "tags"),
            @NamedAttributeNode(value = "component")
          }),
    })
@Entity
@Table(
    name = "UI_LIBRARY",
    uniqueConstraints =
        @UniqueConstraint(
            columnNames = {"TYPE", "NAME"},
            name = "UK_LIBRARY_NAME"))
@EntityListeners(value = {OplogListener.class})
public class Library extends BaseBusEntity implements OplogDataCollector {
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20, nullable = false)
  private LibraryType type;
  /** 名称 */
  @Column(name = "NAME", length = 60)
  private String name;
  /** 描述 */
  @Column(name = "DESCRIPTION")
  private String description;

  /** 库内资源 */
  @OneToMany(
      mappedBy = "library",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
      fetch = FetchType.LAZY)
  private Set<LibraryItem> items;
  /** 所有者 */
  @Any(
      metaColumn =
          @Column(name = "OWNERSHIP_TYPE", length = 10, insertable = false, updatable = false),
      fetch = FetchType.LAZY)
  @AnyMetaDef(
      idType = "long",
      metaType = "string",
      metaValues = {
        @MetaValue(targetEntity = User.class, value = User.OWNERSHIP_KEY),
        @MetaValue(targetEntity = Organization.class, value = Organization.OWNERSHIP_KEY)
      })
  @JoinColumn(name = "OWNERSHIP_ID", insertable = false, updatable = false)
  private Ownership ownership;

  @Override
  @Transient
  public String getEntityName() {
    if (this.type == null) {
      return null;
    }
    return this.type.getName();
  }
}
