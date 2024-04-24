package cn.asany.ui.library.domain;

import cn.asany.ui.library.OplogDataCollector;
import cn.asany.ui.library.dao.listener.OplogListener;
import cn.asany.ui.resources.UIResource;
import cn.asany.ui.resources.domain.Component;
import cn.asany.ui.resources.domain.Icon;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.Hibernate;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@EntityListeners(value = {OplogListener.class})
@NamedEntityGraph(
    name = "Graph.LibraryItem.FetchIcon",
    attributeNodes = {@NamedAttributeNode(value = "tags"), @NamedAttributeNode(value = "icon")})
@Entity
@Table(name = "UI_LIBRARY_ITEM")
public class LibraryItem extends BaseBusEntity implements OplogDataCollector {
  @Id
  @Column(name = "ID")
  @TableGenerator
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "LIBRARY_ID",
      foreignKey = @ForeignKey(name = "FK_LIBRARY_ITEM_LID"),
      nullable = false)
  @ToString.Exclude
  private Library library;

  /** 标签 */
  @ElementCollection
  @CollectionTable(
      name = "UI_LIBRARY_ITEM_TAGS",
      foreignKey = @ForeignKey(name = "FK_LIBRARY_ITEM_TAG"),
      joinColumns = @JoinColumn(name = "ITEM_ID"))
  @Column(name = "TAG")
  private List<String> tags;

  /** 资源ID */
  @Column(name = "RESOURCE_ID", length = 20)
  private Long resourceId;

  /** 资源类型 */
  @Column(name = "RESOURCE_TYPE", length = 10)
  private String resourceType;

  //  @Any(
  //      metaColumn =
  //          @Column(name = "RESOURCE_TYPE", length = 10, insertable = false, updatable = false),
  //      fetch = FetchType.LAZY)
  //  @AnyMetaDef(
  //      idType = "long",
  //      metaType = "string",
  //      metaValues = {
  //        @MetaValue(targetEntity = Icon.class, value = Icon.RESOURCE_NAME),
  //        @MetaValue(targetEntity = Component.class, value = Component.RESOURCE_NAME)
  //      })
  //  @JoinColumn(name = "RESOURCE_ID", insertable = false, updatable = false)
  private transient UIResource resource;

  /** 用于级联加载 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "RESOURCE_ID",
      foreignKey = @ForeignKey(name = "NONE", value = ConstraintMode.NO_CONSTRAINT),
      insertable = false,
      updatable = false)
  @ToString.Exclude
  private Icon icon;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "RESOURCE_ID",
      foreignKey = @ForeignKey(name = "NONE", value = ConstraintMode.NO_CONSTRAINT),
      insertable = false,
      updatable = false)
  @ToString.Exclude
  private Component component;

  @JsonIgnore
  public <T extends UIResource> T getResource(Class<T> resourceClass) {
    return null; // (T) this.resource;
  }

  @Override
  public Class getEntityClass() {
    if (Icon.RESOURCE_NAME.equals(this.resourceType)) {
      return Icon.class;
    }
    return null;
  }

  @Override
  public String getEntityName() {
    if (Icon.RESOURCE_NAME.equals(this.resourceType)) {
      return Icon.class.getSimpleName();
    }
    return null;
  }

  @Override
  public Object getPrimarykey() {
    return this.resourceId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    LibraryItem item = (LibraryItem) o;
    return id != null && Objects.equals(id, item.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
