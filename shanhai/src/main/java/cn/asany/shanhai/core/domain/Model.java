package cn.asany.shanhai.core.domain;

import cn.asany.shanhai.core.domain.enums.ModelConnectType;
import cn.asany.shanhai.core.domain.enums.ModelStatus;
import cn.asany.shanhai.core.domain.enums.ModelType;
import cn.asany.shanhai.gateway.domain.ModelGroupResource;
import cn.asany.shanhai.gateway.domain.Service;
import cn.asany.shanhai.gateway.util.GraphQLFieldArgument;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.*;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.StringArrayConverter;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@NamedEntityGraph(
    name = "Graph.Model.FetchMetadataAndFields",
    attributeNodes = {
      @NamedAttributeNode(value = "metadata"),
      @NamedAttributeNode(value = "fields", subgraph = "SubGraph.ModelField.FetchAttributes")
    },
    subgraphs = {
      @NamedSubgraph(
          name = "SubGraph.ModelField.FetchAttributes",
          attributeNodes = {
            @NamedAttributeNode(value = "metadata"),
            @NamedAttributeNode(value = "delegate"),
            @NamedAttributeNode(value = "arguments")
          })
    })
@NamedEntityGraph(
    name = "Model.Graph",
    attributeNodes = {@NamedAttributeNode("fields")})
@Entity
@Table(name = "SH_MODEL")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Model extends BaseBusEntity implements ModelGroupResource {
  @Id
  @Column(name = "ID")
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 编码 用于 HQL 名称及 API 名称 */
  @Column(name = "CODE", length = 50, unique = true)
  private String code;
  /** 类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20)
  private ModelType type;
  /** 名称 */
  @Column(name = "NAME", length = 100)
  private String name;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 500)
  private String description;
  /** 状态：草稿、发布 */
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", length = 20)
  private ModelStatus status;
  /** 实现接口 */
  @ManyToMany(fetch = FetchType.LAZY)
  @LazyCollection(LazyCollectionOption.EXTRA)
  @JoinTable(
      name = "SH_MODEL_IMPLEMENT",
      joinColumns = @JoinColumn(name = "MODEL_ID"),
      inverseJoinColumns =
          @JoinColumn(
              name = "INTERFACE_ID",
              foreignKey = @ForeignKey(name = "FK_MODEL_IMPLEMENT_IID")),
      foreignKey = @ForeignKey(name = "FK_MODEL_IMPLEMENT_MID"))
  @ToString.Exclude
  private Set<Model> implementz;
  /** UNION 包含的类型 */
  @ManyToMany(fetch = FetchType.LAZY)
  @LazyCollection(LazyCollectionOption.EXTRA)
  @JoinTable(
      name = "SH_MODEL_MEMBER_TYPE",
      joinColumns = @JoinColumn(name = "MODEL_ID"),
      inverseJoinColumns =
          @JoinColumn(
              name = "MEMBER_TYPE",
              foreignKey = @ForeignKey(name = "FK_MODEL_MEMBER_TYPE_TID")),
      foreignKey = @ForeignKey(name = "FK_MODEL_MEMBER_TYPE_MID"))
  @ToString.Exclude
  private Set<Model> memberTypes;

  /** 关联引用 */
  @OneToMany(
      mappedBy = "model",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
      fetch = FetchType.LAZY)
  @ToString.Exclude
  private Set<ModelRelation> relations;
  /** 标签 */
  @Convert(converter = StringArrayConverter.class)
  @Column(name = "LABELS", columnDefinition = "JSON")
  private String[] labels;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "SH_MODEL_FEATURE_RELATION",
      joinColumns = {@JoinColumn(name = "MODEL_ID")},
      inverseJoinColumns = {@JoinColumn(name = "FEATURE_ID")},
      foreignKey = @ForeignKey(name = "FK_MODEL_FEATURE_RELATION_MID"))
  @ToString.Exclude
  private Set<ModelFeature> features;

  @OrderBy("sort asc ")
  @OneToMany(
      mappedBy = "model",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
      fetch = FetchType.LAZY)
  @ToString.Exclude
  private Set<ModelField> fields;

  @OneToMany(
      mappedBy = "model",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
      fetch = FetchType.LAZY)
  @ToString.Exclude
  private Set<ModelEndpoint> endpoints;
  /** 元数据 */
  @OneToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.ALL})
  @LazyToOne(LazyToOneOption.NO_PROXY)
  @PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "MODEL_ID")
  @ToString.Exclude
  private ModelMetadata metadata;
  /** 服务 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "SERVICE_ID", foreignKey = @ForeignKey(name = "FK_MODEL_SID"))
  @ToString.Exclude
  private Service service;
  /** 模块 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "MODULE_ID",
      updatable = false,
      foreignKey = @ForeignKey(name = "FK_MODEL_MODULE_ID"))
  @ToString.Exclude
  private Module module;

  @Transient
  public void connect(Model model, ModelConnectType connectType) {
    if (this.relations == null) {
      this.relations = new HashSet<>();
    }
    Optional<ModelRelation> optional =
        this.relations.stream()
            .filter(
                item ->
                    item.getInverse().getCode().equals(model.getCode())
                        && item.getRelation().equals(connectType.relation)
                        && item.getType().equals(connectType.type))
            .findAny();
    if (optional.isPresent()) {
      return;
    }
    this.relations.add(
        ModelRelation.builder()
            .type(connectType.type)
            .relation(connectType.relation)
            .inverse(model)
            .model(this)
            .build());
  }

  public static class ModelBuilder {
    public ModelBuilder metadata(String tableName) {
      if (this.metadata == null) {
        this.metadata = ModelMetadata.builder().databaseTableName(tableName).build();
      } else {
        this.metadata.setDatabaseTableName(tableName);
      }
      return this;
    }

    public ModelBuilder implementz(List<String> types) {
      this.implementz =
          types.stream()
              .map(item -> Model.builder().code(item).build())
              .collect(Collectors.toSet());
      return this;
    }

    public ModelBuilder memberTypes(List<String> types) {
      this.memberTypes =
          types.stream()
              .map(item -> Model.builder().code(item).build())
              .collect(Collectors.toSet());
      return this;
    }

    public ModelBuilder metadata(ModelMetadata metadata) {
      this.metadata = metadata;
      return this;
    }

    public ModelBuilder features(String... features) {
      this.features =
          Arrays.stream(features)
              .map(id -> ModelFeature.builder().id(id).build())
              .collect(Collectors.toSet());
      return this;
    }

    public ModelBuilder field(Long id, String code, String name, String type) {
      if (this.fields == null) {
        this.fields = new HashSet<>();
      }
      this.fields.add(ModelField.builder().id(id).code(code).name(name).type(type).build());
      return this;
    }

    public ModelBuilder field(
        Long id, String code, String name, Model type, List<GraphQLFieldArgument> arguments) {
      if (this.fields == null) {
        this.fields = new HashSet<>();
      }
      ModelField.ModelFieldBuilder fieldBuilder =
          ModelField.builder().id(id).code(code).name(name).type(type.getCode()).realType(type);
      for (GraphQLFieldArgument argument : arguments) {
        fieldBuilder =
            fieldBuilder.argument(argument.getId(), argument.getType(), argument.getDescription());
      }
      this.fields.add(fieldBuilder.build());
      return this;
    }

    public ModelBuilder field(
        String code,
        String name,
        String type,
        boolean list,
        boolean required,
        List<GraphQLFieldArgument> arguments) {
      if (this.fields == null) {
        this.fields = new HashSet<>();
      }
      ModelField.ModelFieldBuilder fieldBuilder =
          ModelField.builder().code(code).name(name).list(list).required(required).type(type);
      for (GraphQLFieldArgument argument : arguments) {
        fieldBuilder.argument(argument.getId(), argument.getType(), argument.getDescription());
      }
      this.fields.add(fieldBuilder.build());
      return this;
    }

    public ModelBuilder module(Long moduleId) {
      this.module = Module.builder().id(moduleId).build();
      return this;
    }

    public ModelBuilder field(String code, String name, String type) {
      if (this.fields == null) {
        this.fields = new HashSet<>();
      }
      this.fields.add(ModelField.builder().code(code).name(name).type(type).build());
      return this;
    }

    public ModelBuilder field(String code, String name, Model type) {
      if (this.fields == null) {
        this.fields = new HashSet<>();
      }
      this.fields.add(
          ModelField.builder().code(code).name(name).type(type.getCode()).realType(type).build());
      return this;
    }

    public ModelBuilder fields(Set<ModelField> fields) {
      this.fields = fields;
      return this;
    }

    public ModelBuilder fields(ModelField... fields) {
      if (this.fields == null) {
        this.fields = new HashSet<>();
      }
      this.fields.addAll(Arrays.asList(fields));
      return this;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Model model = (Model) o;
    return id != null && Objects.equals(id, model.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
