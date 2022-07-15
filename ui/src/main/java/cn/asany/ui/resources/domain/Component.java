package cn.asany.ui.resources.domain;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.converter.FileObjectConverter;
import cn.asany.ui.resources.UIResource;
import cn.asany.ui.resources.domain.converter.ComponentDataConverter;
import cn.asany.ui.resources.domain.enums.ComponentScope;
import cn.asany.ui.resources.domain.enums.ComponentType;
import cn.asany.ui.resources.domain.toy.ComponentData;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.MapConverter;
import org.jfantasy.framework.util.common.ClassUtil;

/**
 * 组件
 *
 * @author limaofeng
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "NUWA_COMPONENT")
public class Component extends BaseBusEntity implements UIResource {

  public static final String RESOURCE_NAME = "COMPONENT";

  public static final String METADATA_LIBRARY_ID = "library";

  @Id
  @Column(name = "ID", length = 50, updatable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 使用范围 */
  @Enumerated(EnumType.STRING)
  @Column(name = "SCOPE", length = 50, nullable = false)
  private ComponentScope scope;
  /** 图片 */
  @Column(name = "IMAGE", length = 500, columnDefinition = "JSON")
  @Convert(converter = FileObjectConverter.class)
  private FileObject image;
  /** 组件类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 50)
  private ComponentType type;
  /** 名称 */
  @Column(name = "NAME", length = 150)
  private String name;
  /** 名称 */
  @Column(name = "TITLE", length = 150)
  private String title;
  /** 描述 */
  @Column(name = "DESCRIPTION")
  private String description;
  /** 组件模版 */
  @Column(name = "TEMPLATE")
  private String template;
  /** 组件数据 */
  @Convert(converter = ComponentDataConverter.class)
  @Column(name = "BLOCKS", columnDefinition = "JSON")
  private List<ComponentData> blocks;
  /** 元数据 */
  @Convert(converter = MapConverter.class)
  @Column(name = "METADATA", columnDefinition = "Text")
  private Map<String, String> metadata;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Component component = (Component) o;
    return id != null && Objects.equals(id, component.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  /** 标签 */
  private transient List<String> tags;

  @Override
  public List<String> getTags() {
    return tags;
  }

  @Override
  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  @JsonAnySetter
  public void set(String key, String value) {
    if (this.metadata == null) {
      this.metadata = new HashMap<>();
    }
    this.metadata.put(key, value);
  }

  @Transient
  public <T> T get(String key, Class<T> toClass) {
    String value = this.get(key);
    if (value == null) {
      return null;
    }
    return ClassUtil.newInstance(toClass, value);
  }

  @Transient
  public String get(String key) {
    if (this.metadata == null || !this.metadata.containsKey(key)) {
      return null;
    }
    return this.metadata.getOrDefault(key, "");
  }
}
