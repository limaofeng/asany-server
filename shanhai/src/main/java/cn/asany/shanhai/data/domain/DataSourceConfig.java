package cn.asany.shanhai.data.domain;

import cn.asany.shanhai.data.engine.IDataSourceOptions;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.jackson.JSON;

/**
 * 数据源
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
@Table(name = "DATA_SOURCE_CONFIG")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DataSourceConfig extends BaseBusEntity {
  @Id
  @Column(name = "ID", precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 名称 */
  @Column(name = "NAME")
  private String name;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 50)
  private String description;
  /** 类型 */
  @Column(name = "TYPE", length = 20)
  private String type;
  /** 配置详情 */
  @Column(name = "CONFIG_STORE", columnDefinition = "JSON")
  private String details;

  @Transient
  public IDataSourceOptions getOptions(Class<IDataSourceOptions> optionsClass) {
    return JSON.deserialize(this.getDetails(), optionsClass);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    DataSourceConfig that = (DataSourceConfig) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  public static class DataSourceConfigBuilder {
    public DataSourceConfigBuilder options(IDataSourceOptions options) {
      this.details = JSON.serialize(options);
      return this;
    }
  }
}
