package cn.asany.openapi.domain;

import cn.asany.openapi.IOpenApiConfig;
import cn.asany.openapi.domain.enums.OpenApiType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.jackson.JSON;

/**
 * Open Api 配置
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
@Table(
    name = "OPEN_API_CONFIG",
    uniqueConstraints =
        @UniqueConstraint(
            name = "UK_OPEN_API_CONFIG_APPID",
            columnNames = {"TYPE", "APPID"}))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OpenApiConfig extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20, nullable = false)
  private OpenApiType type;
  /** 名称 */
  @Column(name = "NAME", length = 100, nullable = false)
  private String name;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 500)
  private String description;
  /** APPID 唯一值, 方便检索配置 */
  @Column(name = "APPID", length = 100, nullable = false)
  private String appid;
  /** 存放配置参数 */
  @Column(name = "CONFIG_STORE", columnDefinition = "JSON")
  private String details;

  @Transient
  public <T extends IOpenApiConfig> T toConfig(Class<T> tClass) {
    return JSON.deserialize(this.getDetails(), tClass);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    OpenApiConfig store = (OpenApiConfig) o;
    return id != null && Objects.equals(id, store.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
