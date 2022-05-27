package cn.asany.ui.resources.domain;

import cn.asany.ui.resources.domain.converter.ComponentDataConverter;
import cn.asany.ui.resources.domain.enums.ComponentScope;
import cn.asany.ui.resources.domain.enums.ComponentType;
import cn.asany.ui.resources.domain.toy.ComponentData;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

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
public class Component extends BaseBusEntity {

  public static final String RESOURCE_NAME = "COMPONENT";

  @Id
  @Column(name = "ID", length = 50, updatable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 使用范围 */
  @Enumerated(EnumType.STRING)
  @Column(name = "SCOPE", length = 50, nullable = false)
  private ComponentScope scope;
  /** 组件类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 50)
  private ComponentType type;
  /** 名称 */
  @Column(name = "CODE")
  private String code;
  /** 名称 */
  @Column(name = "NAME")
  private String name;
  /** 组件模版 */
  @Column(name = "TEMPLATE")
  private String template;
  /** 组件数据 */
  @Convert(converter = ComponentDataConverter.class)
  @Column(name = "BLOCKS", columnDefinition = "JSON")
  private List<ComponentData> blocks;

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
}
