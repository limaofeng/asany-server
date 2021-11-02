package cn.asany.storage.data.bean;

import cn.asany.storage.api.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 文件信息表
 *
 * @author 软件
 */
@Data
@Entity
@Table(
    name = "STORAGE_FILEOBJECT",
    uniqueConstraints =
        @UniqueConstraint(
            name = "UK_STORAGE_FILEOBJECT",
            columnNames = {"STORAGE_ID", "PATH"}))
@EqualsAndHashCode(callSuper = false, of = "path")
@NoArgsConstructor
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
public class FileDetail extends BaseBusEntity implements Cloneable, FileObject {

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
  /** 是否为文件夹 */
  @Column(name = "IS_DIRECTORY", nullable = false)
  private Boolean isDirectory;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 150)
  private String description;
  /** 文件长度 */
  @Column(name = "LENGTH")
  private Long length;
  /** 文件MD5码 */
  @Column(name = "MD5", length = 50, nullable = false)
  private String md5;
  /** 文件修改时间 */
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "LAST_MODIFIED")
  private Date lastModified;
  /** 文件夹 */
  @JoinColumn(
      name = "PARENT_ID",
      updatable = false,
      foreignKey = @ForeignKey(name = "FK_STORAGE_FILEOBJECT_PARENT"))
  @ManyToOne(fetch = FetchType.LAZY)
  private FileDetail parentFile;
  /** 获取子目录列表 */
  @OneToMany(mappedBy = "parentFile", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @OrderBy("createdAt ASC")
  private List<FileDetail> children;
  /** 文件命名空间 */
  @JoinColumn(
      name = "STORAGE_ID",
      nullable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "FK_STORAGE_FILEOBJECT_STORAGE"))
  @ManyToOne(fetch = FetchType.LAZY)
  private StorageConfig storageConfig;

  @Embedded private FileMetadata metadata;

  public FileDetail(Long id) {
    this.id = id;
  }

  public FileDetail(String path) {
    this.path = path;
  }

  @Override
  public boolean isDirectory() {
    return this.isDirectory;
  }

  @Override
  public long getSize() {
    return this.length;
  }

  @Override
  public FileDetail getParentFile() {
    return this.parentFile;
  }

  @Override
  public List<FileObject> listFiles() {
    throw new RuntimeException("FileDetails 不提供该方法");
  }

  @Override
  public String getPath() {
    return this.path;
  }

  @Override
  public Date lastModified() {
    return this.lastModified;
  }

  @Override
  public List<FileObject> listFiles(FileItemFilter filter) {
    throw new RuntimeException("FileDetails 不提供该方法");
  }

  @Override
  public List<FileObject> listFiles(FileItemSelector selector) {
    throw new RuntimeException("FileDetails 不提供该方法");
  }

  @Override
  public FileObjectMetadata getMetadata() {
    throw new RuntimeException("FileDetails 不提供该方法");
  }

  @Override
  @Transient
  public Storage getStorage() {
    return null;
  }

  @Override
  public InputStream getInputStream() throws IOException {
    throw new RuntimeException("FileDetails 不提供该方法");
  }
}
