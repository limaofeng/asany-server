package cn.asany.shanhai.core.domain;

import cn.asany.shanhai.gateway.domain.ModelGroupResource;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.jackson.JSON;

/**
 * 实体字段
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
@NamedEntityGraph(
    name = "Graph.ModelField.FetchModelAndType",
    attributeNodes = {
      @NamedAttributeNode(value = "model"),
      @NamedAttributeNode(value = "type"),
    })
@Table(
    name = "SH_MODEL_FIELD",
    uniqueConstraints =
        @UniqueConstraint(
            columnNames = {"MODEL_ID", "CODE"},
            name = "UK_MODEL_FIELD_CODE"))
public class ModelField extends BaseBusEntity implements ModelGroupResource {

  /** id主键 */
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 编码 用于 HQL 名称及 API 名称 */
  @Column(name = "CODE", length = 50)
  private String code;
  /** 名称 */
  @Column(name = "NAME", length = 100)
  private String name;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 500)
  private String description;
  /** 默认值 */
  @Column(name = "DEFAULT_VALUE", length = 100)
  private String defaultValue;
  /** 是否必填 */
  @Builder.Default
  @Column(name = "IS_REQUIRED")
  private Boolean required = false;
  /** 是否主键 */
  @Builder.Default
  @Column(name = "IS_PRIMARY_KEY", length = 10, nullable = false)
  private Boolean primaryKey = false;
  /** 字段类型 */
  @Column(name = "TYPE", length = 50, nullable = false)
  private String type;
  /** 关联的具体字段类型 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "TYPE_ID", foreignKey = @ForeignKey(name = "FK_MODEL_FIELD_TID"))
  @ToString.Exclude
  private Model realType;
  /** 是否唯一 */
  @Builder.Default
  @Column(name = "IS_UNIQUE", length = 1)
  private Boolean unique = false;
  /** 存储值为列表，而不是单个值 */
  @Builder.Default
  @Column(name = "IS_LIST", length = 1, updatable = false)
  private Boolean list = false;
  /** 是否系统字段 */
  @Builder.Default
  @Column(name = "IS_SYSTEM", updatable = false, length = 1)
  private Boolean system = false;
  /** 序号 */
  @Column(name = "SORT")
  private Integer sort;
  /** 实体 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "MODEL_ID",
      foreignKey = @ForeignKey(name = "FK_MODEL_FIELD_MODEL_ID"),
      updatable = false,
      nullable = false)
  @LazyToOne(LazyToOneOption.NO_PROXY)
  @ToString.Exclude
  private Model model;
  /** 元数据 */
  @OneToOne(
      mappedBy = "field",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
  private ModelFieldMetadata metadata;
  /** 委派 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "DELEGATE_ID", foreignKey = @ForeignKey(name = "FK_MODEL_FIELD_DID"))
  @ToString.Exclude
  private ModelDelegate delegate;
  /** 参数 */
  @OneToMany(
      mappedBy = "field",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
      fetch = FetchType.LAZY)
  @ToString.Exclude
  private Set<ModelFieldArgument> arguments;

  public static class ModelFieldBuilder {

    public ModelFieldBuilder() {
      this.arguments = new HashSet<>();
    }

    public ModelFieldBuilder metadata(boolean insertable, boolean updatable) {
      this.metadata =
          ModelFieldMetadata.builder().insertable(insertable).updatable(updatable).build();
      return this;
    }

    public ModelFieldBuilder metadata(boolean insertable, boolean updatable, boolean sortable) {
      this.metadata =
          ModelFieldMetadata.builder()
              .insertable(insertable)
              .updatable(updatable)
              .sortable(sortable)
              .build();
      return this;
    }

    public ModelFieldBuilder metadata(String columnName) {
      this.metadata = ModelFieldMetadata.builder().databaseColumnName(columnName).build();
      return this;
    }

    public ModelFieldBuilder realType(Model type) {
      this.realType = type;
      return this;
    }

    public ModelFieldBuilder argument(String name, String type) {
      if (this.arguments == null) {
        this.arguments = new HashSet<>();
      }
      this.arguments.add(ModelFieldArgument.builder().name(name).type(type).build());
      return this;
    }

    public ModelFieldBuilder argument(String name, Model type, String description) {
      if (this.arguments == null) {
        this.arguments = new HashSet<>();
      }
      this.arguments.add(
          ModelFieldArgument.builder().name(name).type(type).description(description).build());
      return this;
    }

    public ModelFieldBuilder argument(String name, String type, Boolean required) {
      if (this.arguments == null) {
        this.arguments = new HashSet<>();
      }
      this.arguments.add(
          ModelFieldArgument.builder().name(name).required(required).type(type).build());
      return this;
    }

    public ModelFieldBuilder argument(String name, String type, String description) {
      if (this.arguments == null) {
        this.arguments = new HashSet<>();
      }
      this.arguments.add(
          ModelFieldArgument.builder().name(name).description(description).type(type).build());
      return this;
    }

    public ModelFieldBuilder argument(
        String name, String type, String description, Object defaultValue) {
      if (this.arguments == null) {
        this.arguments = new HashSet<>();
      }
      this.arguments.add(
          ModelFieldArgument.builder()
              .name(name)
              .description(description)
              .type(type)
              .defaultValue(JSON.serialize(defaultValue))
              .build());
      return this;
    }

    public ModelFieldBuilder argument(
        String name, Model type, String description, Object defaultValue) {
      if (this.arguments == null) {
        this.arguments = new HashSet<>();
      }
      this.arguments.add(
          ModelFieldArgument.builder()
              .name(name)
              .description(description)
              .type(type)
              .defaultValue(JSON.serialize(defaultValue))
              .build());
      return this;
    }

    public ModelFieldBuilder argument(
        String name, String type, Boolean required, String description, Object defaultValue) {
      if (this.arguments == null) {
        this.arguments = new HashSet<>();
      }
      this.arguments.add(
          ModelFieldArgument.builder()
              .name(name)
              .required(required)
              .description(description)
              .type(type)
              .defaultValue(JSON.serialize(defaultValue))
              .build());
      return this;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    ModelField field = (ModelField) o;
    return id != null && Objects.equals(id, field.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
