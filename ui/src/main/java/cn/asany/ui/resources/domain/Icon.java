package cn.asany.ui.resources.domain;

import cn.asany.ui.library.dao.listener.OplogListener;
import cn.asany.ui.resources.UIResource;
import cn.asany.ui.resources.domain.enums.IconType;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import jakarta.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import net.asany.jfantasy.framework.dao.hibernate.converter.MapConverter;
import net.asany.jfantasy.framework.util.common.ClassUtil;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "UI_ICON")
@EntityListeners(value = {OplogListener.class})
public class Icon extends BaseBusEntity implements UIResource {

  public static final String RESOURCE_NAME = "ICON";
  public static final String METADATA_LIBRARY_ID = "library";

  @Id
  @Column(name = "ID")
  @TableGenerator
  private Long id;

  /** 类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20, nullable = false)
  private IconType type;

  /** 编码 */
  @Column(name = "UNICODE", length = 50)
  private String unicode;

  /** 名称 */
  @Column(name = "NAME", length = 60)
  private String name;

  /** 描述 */
  @Column(name = "DESCRIPTION")
  private String description;

  /** 内容 */
  @Column(name = "CONTENT", columnDefinition = "TEXT")
  private String content;

  /** 元数据 */
  @Convert(converter = MapConverter.class)
  @Column(name = "METADATA", columnDefinition = "Text")
  private Map<String, String> metadata;

  /** 标签 */
  private transient List<String> tags;

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
