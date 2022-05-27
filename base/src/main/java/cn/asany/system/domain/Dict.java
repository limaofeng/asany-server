package cn.asany.system.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.util.common.ObjectUtil;

/**
 * 数据字典类 <br>
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
@Table(name = "SYS_DICT")
@IdClass(DictKey.class)
public class Dict extends BaseBusEntity {

  public static final String PATH_SEPARATOR = "/";
  /** 代码 */
  @Id private String code;
  /** 配置类别 */
  @Id private String type;
  /** 名称 */
  @Column(name = "NAME", length = 50, nullable = false)
  private String name;
  /** 路径 */
  @Column(name = "PATH", nullable = false, length = 200)
  private String path;
  /** 排序字段 */
  @Column(name = "SORT", nullable = false)
  private Integer index;
  /** 层级 */
  @Column(name = "LEVEL", nullable = false)
  private Integer level;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 200)
  private String description;
  /** 上级数据字典 */
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
  @JoinColumns(
      value = {
        @JoinColumn(
            name = "PCODE",
            referencedColumnName = "CODE",
            foreignKey = @ForeignKey(name = "FK_DICT_PCODE")),
        @JoinColumn(
            name = "PTYPE",
            referencedColumnName = "TYPE",
            foreignKey = @ForeignKey(name = "FK_DICT_PTYPE"))
      },
      foreignKey = @ForeignKey(name = "FK_DICT_PARENT"))
  @ToString.Exclude
  private Dict parent;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "TYPE",
      foreignKey = @ForeignKey(name = "FK_DICT_TYPE"),
      insertable = false,
      updatable = false,
      nullable = false)
  @ToString.Exclude
  private DictType dictType;

  /** 下级数据字典 */
  @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @OrderBy("index ASC")
  @ToString.Exclude
  private List<Dict> children;

  @Transient private DictKey id;
  @Transient private DictKey parentId;

  public DictKey getId() {
    return ObjectUtil.defaultValue(id, id = DictKey.newInstance(this.code, this.type));
  }

  public void setId(DictKey key) {
    this.id = key;
    this.setCode(key.getCode());
    this.setType(key.getType());
  }

  public DictKey getParentKey() {
    if (this.getParent() == null) {
      return null;
    }
    return ObjectUtil.defaultValue(
        parentId, parentId = DictKey.newInstance(this.parent.code, this.parent.type));
  }

  public void setParentKey(DictKey key) {
    if (key == null) {
      return;
    }
    this.parentId = key;
    this.parent = new Dict();
    this.parent.setCode(key.getCode());
    this.parent.setType(key.getType());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Dict dict = (Dict) o;
    return code != null
        && Objects.equals(code, dict.code)
        && type != null
        && Objects.equals(type, dict.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, type);
  }
}
