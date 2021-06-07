package cn.asany.storage.data.bean;

import cn.asany.storage.core.FileStoreException;
import cn.asany.storage.core.IStorageConfig;
import cn.asany.storage.core.engine.minio.MinIOStorageConfig;
import cn.asany.storage.data.bean.enums.StorageType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.jackson.JSON;

import javax.persistence.*;
import java.util.List;

/**
 * 文件管理器配置表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-7-12 下午02:30:29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "STORAGE_CONFIG")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "folders", "fileDetails"})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StorageConfig extends BaseBusEntity {

    private static final long serialVersionUID = -4833473939396674528L;

    public StorageConfig(String id) {
        this.id = id;
    }

    /**
     * 唯一别名，作为文件管理器的ID
     */
    @Id
    @Column(name = "ID", nullable = false, updatable = false, length = 50)
    private String id;
    /**
     * 文件管理器名称
     */
    @Column(name = "NAME", length = 150, nullable = false)
    private String name;
    /**
     * 文件管理器的类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 20, nullable = false, insertable = true, updatable = false)
    private StorageType type;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION", length = 250)
    private String description;
    /**
     * 存放配置参数
     */
    @Column(name = "CONFIG_STORE", columnDefinition = "JSON")
    private String details;
    /**
     * 文件管理器对应的目录
     */
    @OneToMany(mappedBy = "storage", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Folder> folders;

    /**
     * 文件管理器对应的文件
     */
    @OneToMany(mappedBy = "storage", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<FileDetail> fileDetails;

    @Transient
    public IStorageConfig getProperties() {
        switch (this.getType()) {
            case MINIO:
                MinIOStorageConfig storageConfig = JSON.deserialize(this.getDetails(), MinIOStorageConfig.class);
                storageConfig.setId(this.getId());
                return storageConfig;
            default:
                throw new FileStoreException("不支持的类型");
        }
    }
}
