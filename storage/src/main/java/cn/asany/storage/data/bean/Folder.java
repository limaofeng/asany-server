package cn.asany.storage.data.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 文件夹表
 *
 * @author 软件
 */
@Data
@Entity
@Table(name = "STORAGE_FOLDER")
@EqualsAndHashCode(callSuper = false)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class Folder extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @Column(name = "PATH", nullable = false, updatable = false, length = 250)
  private String path;
  /** 同一目录下不允许重名 */
  @Column(name = "NAME")
  private String name;
  /** 文件夹类型:0.系统,1.公共,2.组织（分公司）,3.个人 */
  @Column(name = "TYPE")
  private String type;
  /** 文件夹类型:当为公司或者个人时，输入公司和个人的ID */
  @Column(name = "TYPE_VALUE")
  private String typeValue;
  /** 允许上传的文件扩展名 */
  @Column(name = "EXTS")
  private String exts;
  /** 可上传文件大小 */
  @Column(name = "LENGTH")
  private Long size;
  /** 是否为叶子目录,叶子目录不能创建子目录 */
  @Column(name = "LAST")
  private boolean last;
  /** 父目录ID */
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
  @JoinColumn(name = "PID", foreignKey = @ForeignKey(name = "FK_STORAGE_FOLDER_PID"))
  private Folder parentFolder;
  /** 存储器 */
  @JoinColumn(
      name = "STORAGE_ID",
      updatable = false,
      foreignKey = @ForeignKey(name = "FK_STORAGE_FOLDER_STORAGE"))
  @ManyToOne(fetch = FetchType.LAZY)
  private StorageConfig storage;
  /** 获取子目录列表 */
  @OneToMany(mappedBy = "parentFolder", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @OrderBy("createdAt ASC")
  private List<Folder> children;
  /** 附件对应的集合 */
  @OneToMany(mappedBy = "folder", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<FileDetail> fileObjects;

  public void setStorage(String storageId) {
    this.setStorage(StorageConfig.builder().id(storageId).build());
  }

  public void setStorage(StorageConfig storage) {
    this.storage = storage;
  }
}
