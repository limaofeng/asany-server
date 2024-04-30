/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.storage.data.domain;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.Storage;
import cn.asany.storage.api.StorageSpace;
import cn.asany.storage.core.engine.virtual.VirtualFileObject;
import cn.asany.storage.core.engine.virtual.VirtualStorage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import org.hibernate.Hibernate;

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
  @TableGenerator
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

  @Column(name = "STORAGE_ID", nullable = false, updatable = false, length = 50)
  private String storageConfig;

  /** 文件命名空间 */
  //  @JoinColumn(
  //      name = "STORAGE_ID",
  //      nullable = false,
  //      foreignKey = @ForeignKey(name = "FK_STORAGE_FILEOBJECT_STORAGE"))
  //  @ManyToOne(fetch = FetchType.LAZY)
  //  @ToString.Exclude
  //  private StorageConfig storageConfig;

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
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    FileDetail that = (FileDetail) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
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
    builder.storage(space, this.getStorageConfig());
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
      this.storageConfig = ObjectUtil.defaultValue(id, Storage.DEFAULT_STORAGE_ID);
      return this;
    }
  }
}
