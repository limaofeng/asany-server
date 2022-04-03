package cn.asany.storage.data.bean;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.Storage;
import cn.asany.storage.api.StorageSpace;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.StringSetConverter;

/**
 * 文件上传时，为其指定的上传目录
 *
 * <p>通过Key获取上传的目录及文件管理器
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-7-23 上午10:30:06
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "STORAGE_SPACE")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class Space extends BaseBusEntity implements StorageSpace {

  private static final long serialVersionUID = 8150927437017643578L;

  @Id
  @Column(name = "ID", nullable = false, updatable = false, length = 50, unique = true)
  private String id;
  /** 目录名称 */
  @Column(name = "NAME", length = 250)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "FOLDER_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_STORAGE_SPACE_FOLDER_ID"))
  @ToString.Exclude
  private FileDetail vFolder;
  /** 使用的插件 */
  @Convert(converter = StringSetConverter.class)
  @Column(name = "PLUGINS", length = 250)
  private Set<String> plugins;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Space space = (Space) o;
    return id != null && Objects.equals(id, space.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  public FileObject getFolder() {
    return this.vFolder.toFileObject(this);
  }

  @Override
  public Storage getStorage() {
    return null;
  }
}
