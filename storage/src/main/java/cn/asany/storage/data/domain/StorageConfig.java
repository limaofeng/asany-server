package cn.asany.storage.data.domain;

import cn.asany.storage.api.IStorageConfig;
import cn.asany.storage.core.FileStoreException;
import cn.asany.storage.core.engine.minio.MinIOStorageConfig;
import cn.asany.storage.core.engine.oss.OSSStorageConfig;
import cn.asany.storage.data.domain.enums.StorageType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.jackson.JSON;

/**
 * 文件管理器配置表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-7-12 下午02:30:29
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "STORAGE_CONFIG")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "folders", "fileDetails"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StorageConfig extends BaseBusEntity {

  public StorageConfig(String id) {
    this.id = id;
  }

  /** 唯一别名，作为文件管理器的ID */
  @Id
  @Column(name = "ID", nullable = false, updatable = false, length = 50)
  private String id;

  /** 文件管理器名称 */
  @Column(name = "NAME", length = 150, nullable = false)
  private String name;

  /** 文件管理器的类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20, nullable = false, updatable = false)
  private StorageType type;

  /** 描述 */
  @Column(name = "DESCRIPTION", length = 250)
  private String description;

  /** 单位 MB */
  @Column(name = "QUOTA")
  private Long quota;

  /** 存放配置参数 */
  @Column(name = "CONFIG_STORE", columnDefinition = "JSON")
  private String details;

  /** 文件管理器对应的文件 */
  @OneToMany(mappedBy = "storageConfig", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @ToString.Exclude
  private List<FileDetail> fileDetails;

  @Transient
  public IStorageConfig getProperties() {
    switch (this.getType()) {
      case MINIO:
        MinIOStorageConfig minIOStorageConfig =
            JSON.deserialize(this.getDetails(), MinIOStorageConfig.class);
        minIOStorageConfig.setId(this.getId());
        return minIOStorageConfig;
      case OSS:
        OSSStorageConfig ossStorageConfig =
            JSON.deserialize(this.getDetails(), OSSStorageConfig.class);
        ossStorageConfig.setId(this.getId());
        return ossStorageConfig;
      default:
        throw new FileStoreException("不支持的类型");
    }
  }

  public static class StorageConfigBuilder {

    public StorageConfigBuilder type(StorageType type, IStorageConfig config) {
      this.type = type;
      this.details = JSON.serialize(config);
      return this;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    StorageConfig that = (StorageConfig) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
