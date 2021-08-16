package cn.asany.storage.data.bean;

import cn.asany.storage.api.FileItemFilter;
import cn.asany.storage.api.FileItemSelector;
import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.FileObjectMetadata;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 文件信息表
 *
 * @author 软件
 */
@Data
@Entity
@Table(name = "STORAGE_FILEOBJECT")
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({
  "hibernate_lazy_initializer",
  "handler",
  "folder",
  "storage",
  "md5",
  "creator",
  "updator",
  "modifier",
  "createTime",
  "modifyTime"
})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 150, nullable = false)
  private String description;
  /** 文件长度 */
  @Column(name = "LENGTH")
  @JsonProperty("length")
  private Long length;
  /** 文件MD5码 */
  @Column(name = "MD5", length = 50, nullable = false)
  private String md5;

  @JoinColumn(
      name = "FOLDER_ID",
      updatable = false,
      foreignKey = @ForeignKey(name = "FK_STORAGE_FILEOBJECT_FID"))
  @ManyToOne(fetch = FetchType.LAZY)
  private Folder folder;
  /** 文件命名空间 */
  @JoinColumn(
      name = "STORAGE_ID",
      updatable = false,
      foreignKey = @ForeignKey(name = "FK_STORAGE_FILEOBJECT_STORAGE"))
  @ManyToOne(fetch = FetchType.LAZY)
  private StorageConfig storage;

  @Override
  public boolean isDirectory() {
    return false;
  }

  @Override
  public long getSize() {
    return this.length;
  }

  @Override
  public String getContentType() {
    return this.mimeType;
  }

  @Override
  public FileObject getParentFile() {
    throw new RuntimeException("FileDetails 不提供该方法");
  }

  @Override
  public List<FileObject> listFiles() {
    throw new RuntimeException("FileDetails 不提供该方法");
  }

  @Override
  public String getAbsolutePath() {
    return this.path;
  }

  @Override
  public Date lastModified() {
    return this.getUpdatedAt();
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
  public InputStream getInputStream() throws IOException {
    throw new RuntimeException("FileDetails 不提供该方法");
  }
}
