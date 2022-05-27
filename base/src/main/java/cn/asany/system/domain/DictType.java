package cn.asany.system.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 数据字典分类表
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
@Table(name = "SYS_DICT_TYPE")
public class DictType extends BaseBusEntity {
  /** 国家 */
  public static final String TYPE_CODE_COUNTRY = "country";
  /** “省份、城市、区县、乡镇” 四级联动数据 */
  public static final String TYPE_CODE_PCAS = "pcas";
  /** 省份 */
  public static final String TYPE_CODE_PCAS_PROVINCE = "province";
  /** 城市 */
  public static final String TYPE_CODE_PCAS_CITY = "city";
  /** 区县 */
  public static final String TYPE_CODE_PCAS_DISTRICT = "district";
  /** 乡镇/街道 */
  public static final String TYPE_CODE_PCAS_STREET = "street";

  public static final String PATH_SEPARATOR = "/";
  private static final long serialVersionUID = 6196296454047254419L;

  @Id
  @Column(name = "CODE", length = 20)
  private String id;
  /** 名称 */
  @Column(name = "NAME", length = 200)
  private String name;
  /** 层级 */
  @Column(name = "LEVEL", nullable = false)
  private Integer level;
  /** 路径 */
  @Column(name = "PATH", nullable = false, length = 200)
  private String path;
  /** 排序字段 */
  @Column(name = "SORT", nullable = false)
  private Integer index;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 2000)
  private String description;

  @OneToMany(mappedBy = "dictType", fetch = FetchType.LAZY)
  @OrderBy("index ASC")
  @ToString.Exclude
  private List<Dict> dicts;

  /** 上级数据字典分类 */
  @JsonProperty("parent")
  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REFRESH})
  @JoinColumn(name = "PCODE", foreignKey = @ForeignKey(name = "FK_DICT_TYPE_PCODE"))
  @ToString.Exclude
  private DictType parent;

  /** 下级数据字典 */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @OneToMany(
      mappedBy = "parent",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @OrderBy("index ASC")
  @ToString.Exclude
  private List<DictType> children;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    DictType dictType = (DictType) o;
    return id != null && Objects.equals(id, dictType.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
