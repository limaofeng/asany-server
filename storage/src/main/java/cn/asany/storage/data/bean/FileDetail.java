package cn.asany.storage.data.bean;

import cn.asany.storage.api.*;
import cn.asany.storage.core.engine.virtual.VirtualFileObject;
import cn.asany.storage.core.engine.virtual.VirtualStorage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.util.common.ObjectUtil;

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
          name = "UK_STORAGE_FILEOBJECT_PATH",
          columnNames = {"PATH"}),
      @UniqueConstraint(
          name = "UK_STORAGE_FILE_NAME",
          columnNames = {"NAME", "PARENT_ID"})
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
public class FileDetail extends BaseBusEntity implements Cloneable {

  public static String NAME_OF_THE_RECYCLE_BIN = "$RECYCLE.BIN";
  public static String NAME_OF_THE_TEMP_FOLDER = ".temp";

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 虚拟文件路径 */
  @Column(name = "PATH", nullable = false, length = 250)
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
  @Column(name = "LENGTH", nullable = false)
  private Long size;
  /** 文件MD5码 */
  @Column(name = "MD5", length = 50, nullable = false)
  private String md5;
  /** 文件修改时间 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "UPDATED_AT", insertable = false, updatable = false)
  private Date lastModified;
  /** 文件夹 */
  @JoinColumn(name = "PARENT_ID", foreignKey = @ForeignKey(name = "FK_STORAGE_FILEOBJECT_PARENT"))
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
      foreignKey = @ForeignKey(name = "FK_STORAGE_FILEOBJECT_STORAGE"))
  @ManyToOne(fetch = FetchType.LAZY)
  @ToString.Exclude
  private StorageConfig storageConfig;
  /** 文件存储路径 */
  @Column(name = "STORE_PATH", nullable = false, length = 250)
  private String storePath;
  /** 是否隐藏 */
  @Builder.Default
  @Column(name = "HIDDEN", nullable = false)
  private Boolean hidden = Boolean.FALSE;
  /** 所有的缩略图 */
  @OneToMany(mappedBy = "source", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @OrderBy("createdAt ASC")
  @ToString.Exclude
  private List<Thumbnail> thumbnails;

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

  @Transient
  public boolean isRecycleBin() {
    return ObjectUtil.exists(
        this.labels,
        (Predicate<FileLabel>)
            (label) ->
                FileLabel.SYSTEM_NAMESPACE.equals(label.getNamespace())
                    && FileDetail.NAME_OF_THE_RECYCLE_BIN.equals(label.getName()));
  }

  private VirtualFileObject.VirtualFileObjectBuilder buildVirtualFileObject() {
    return VirtualFileObject.builder()
        .id(this.id)
        .path(this.path)
        .storePath(this.storePath)
        .name(this.name)
        .size(this.size)
        .lastModified(this.getUpdatedAt())
        .directory(this.isDirectory)
        .mimeType(this.mimeType)
        .description(this.description)
        .extension(this.extension)
        .metadata(this.md5)
        .labels(labels)
        .createdAt(this.getCreatedAt());
  }

  @Transient
  public FileObject toFileObject() {
    VirtualFileObject.VirtualFileObjectBuilder builder = buildVirtualFileObject();
    return builder.build();
  }

  @Transient
  public FileObject toFileObject(StorageSpace space) {
    VirtualFileObject.VirtualFileObjectBuilder builder = buildVirtualFileObject();
    builder.storage(space, this.getStorageConfig().getId());
    return builder.build();
  }

  @Transient
  public FileObject toFileObject(VirtualStorage storage) {
    VirtualFileObject.VirtualFileObjectBuilder builder = buildVirtualFileObject();
    builder.storage(storage);
    return builder.build();
  }

  public static class FileDetailBuilder {

    public FileDetailBuilder storage(String id) {
      this.storageConfig =
          StorageConfig.builder()
              .id(ObjectUtil.defaultValue(id, Storage.DEFAULT_STORAGE_ID))
              .build();
      return this;
    }
  }
}
