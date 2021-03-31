package cn.asany.shanhai.core.bean;

import cn.asany.shanhai.core.bean.enums.ModelConnectType;
import cn.asany.shanhai.core.bean.enums.ModelStatus;
import cn.asany.shanhai.core.bean.enums.ModelType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.StringArrayConverter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "SH_MODEL")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Model extends BaseBusEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 编码 用于 HQL 名称及 API 名称
     */
    @Column(name = "CODE", length = 50, unique = true)
    private String code;
    /**
     * 类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 10)
    private ModelType type;
    /**
     * 名称
     */
    @Column(name = "NAME", length = 100)
    private String name;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION", length = 200)
    private String description;
    /**
     * 状态：草稿、发布
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 20)
    private ModelStatus status;
    /**
     * 关联引用
     */
    @OneToMany(mappedBy = "model", cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<ModelRelation> relations;
    /**
     * 标签
     */
    @Convert(converter = StringArrayConverter.class)
    @Column(name = "LABELS", columnDefinition = "JSON")
    private String[] labels;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SH_MODEL_FEATURE_RELATION", joinColumns = {@JoinColumn(name = "MODEL_ID")}, inverseJoinColumns = {@JoinColumn(name = "FEATURE_ID")}, foreignKey = @ForeignKey(name = "FK_MODEL_FEATURE_RELATION_MID"))
    private List<ModelFeature> features;

    @OrderBy("sort asc ")
    @OneToMany(mappedBy = "model", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<ModelField> fields;

    @OneToMany(mappedBy = "model", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<ModelEndpoint> endpoints;
    /**
     * 元数据
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @PrimaryKeyJoinColumn
    private ModelMetadata metadata;
    /**
     * 服务
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICE_ID", foreignKey = @ForeignKey(name = "FK_MODEL_SID"))
    private NameServer nameServer;

    @Transient
    public void connect(Model model, ModelConnectType connectType) {
        if (this.relations == null) {
            this.relations = new ArrayList<>();
        }
        Optional<ModelRelation> optional = this.relations.stream().filter(item -> item.getInverse().getCode().equals(model.getCode()) && item.getRelation().equals(connectType.relation) && item.getType().equals(connectType.type)).findAny();
        if (optional.isPresent()) {
            return;
        }
        this.relations.add(ModelRelation.builder().type(connectType.type).relation(connectType.relation).inverse(model).model(this).build());
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

        public ModelBuilder metadata(ModelMetadata metadata) {
            this.metadata = metadata;
            return this;
        }

        public ModelBuilder features(String... features) {
            this.features = Arrays.stream(features).map(id -> ModelFeature.builder().id(id).build()).collect(Collectors.toList());
            return this;
        }

        public ModelBuilder field(Long id, String code, String name, Model type) {
            if (this.fields == null) {
                this.fields = new ArrayList<>();
            }
            this.fields.add(ModelField.builder().id(id).code(code).name(name).type(type).build());
            return this;
        }

        public ModelBuilder field(String code, String name, Model type) {
            if (this.fields == null) {
                this.fields = new ArrayList<>();
            }
            this.fields.add(ModelField.builder().code(code).name(name).type(type).build());
            return this;
        }

        public ModelBuilder fields(List<ModelField> fields) {
            this.fields = fields;
            return this;
        }

        public ModelBuilder fields(ModelField... fields) {
            if (this.fields == null) {
                this.fields = new ArrayList<>();
            }
            this.fields.addAll(Arrays.asList(fields));
            return this;
        }
    }

    @Override
    public String toString() {
        return "Model{" +
            "id=" + id +
            ", code='" + code + '\'' +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", labels=" + Arrays.toString(labels) +
            ", features=" + features +
            ", fields=" + fields +
            '}';
    }
}
