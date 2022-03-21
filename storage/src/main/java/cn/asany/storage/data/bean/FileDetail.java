package cn.asany.storage.data.bean;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.dto.SimpleFileObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusBusinessEntity;

/**
 * 文件信息表
 *
 * @author 软件
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(
    name = "STORAGE_FILEOBJECT",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "UK_STORAGE_FILEOBJECT",
          columnNames = {"STORAGE_ID", "PATH"})
    })
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(
    value = {
      "hibernateLazyInitializer",
      "handler",
      "storage",
      "storageConfig",
      "folder",
      "parentFile",
      "metadata",
      "inputStream"
    })
public class FileDetail extends BaseBusBusinessEntity implements Cloneable {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 虚拟文件路径 */
  @Column(name = "PATH", nullable = false, updatable = false, length = 250)
  private String path;
  /** 文件类型 */
  @Column(name = "MIME_TYPE", length = 50, nullable = false)
  private String mimeType;
  /** 文件名称 */
  @Column(name = "NAME", length = 50, nullable = false)
  private String name;
  /** 文件扩展名 */
  @Column(name = "EXTENSION", length = 50)
  private String extension;
  /** 是否为文件夹 */
  @Column(name = "IS_DIRECTORY", nullable = false)
  private Boolean isDirectory;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 150)
  private String description;
  /** 文件长度 */
  @Column(name = "LENGTH")
  private Long size;
  /** 文件MD5码 */
  @Column(name = "MD5", length = 50, nullable = false)
  private String md5;
  /** 文件修改时间 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "UPDATED_AT", insertable = false, updatable = false)
  private Date lastModified;
  /** 文件夹 */
  @JoinColumn(
      name = "PARENT_ID",
      updatable = false,
      foreignKey = @ForeignKey(name = "FK_STORAGE_FILEOBJECT_PARENT"))
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private FileDetail parentFile;
  /** 获取子目录列表 */
  @OneToMany(mappedBy = "parentFile", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @OrderBy("createdAt ASC")
  @ToString.Exclude
  private List<FileDetail> children;
  /** 文件命名空间 */
  @JoinColumn(
      name = "STORAGE_ID",
      nullable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "FK_STORAGE_FILEOBJECT_STORAGE"))
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private StorageConfig storageConfig;
  //  /** 标记加心 */
  //  @Builder.Default
  //  @Column(name = "STARRED")
  //  private Boolean starred = false;
  //  /** 标记回收 */
  //  @Builder.Default
  //  @Column(name = "TRASHED")
  //  private Boolean trashed = false;

  @OneToMany(
      mappedBy = "file",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private Set<FileLabel> labels;

  @Embedded private FileMetadata metadata;

  public FileDetail(Long id) {
    this.id = id;
  }

  public FileDetail(String path) {
    this.path = path;
  }

  @Override
  public FileDetail clone() {
    try {
      FileDetail clone = (FileDetail) super.clone();
      return FileDetail.builder()
          .name(clone.getName())
          .size(clone.getSize())
          .isDirectory(clone.getIsDirectory())
          .description(clone.getDescription())
          .md5(clone.getMd5())
          .mimeType(clone.getMimeType())
          .build();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    FileDetail that = (FileDetail) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Transient
  public boolean isRootFolder() {
    return this.parentFile == null;
  }

  public FileObject toFileObject() {
    SimpleFileObject.SimpleFileObjectBuilder builder =
        SimpleFileObject.builder()
            .id(this.id)
            .path(this.path)
            .name(this.name)
            .size(this.size)
            .lastModified(this.getUpdatedAt())
            .directory(this.isDirectory)
            .mimeType(this.mimeType)
            .metadata(this.md5)
            .addUserMetadata("DESCRIPTION", this.description)
            .addUserMetadata("EXTENSION", this.extension)
            .addUserMetadata("CREATED_AT", this.getCreatedAt());
    if (this.getStorageConfig() != null) {
      builder.storage(new SimpleFileObject.SimpleStorage(this.getStorageConfig().getId()));
    }
    return builder.build();
  }
}
