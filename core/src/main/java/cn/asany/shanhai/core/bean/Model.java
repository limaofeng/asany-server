package cn.asany.shanhai.core.bean;

import cn.asany.shanhai.core.bean.enums.ModelStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.StringArrayConverter;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
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
     * 数据源
     */
    @Column(name = "DATA_SOURCE", length = 20)
    private String dataSource;
    /**
     * 状态：草稿、发布
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 20)
    private ModelStatus status;
    /**
     * 是否系统
     */
    @Column(name = "IS_SYSTEM")
    private Boolean isSystem;

//    private List<> relations;
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
    @OneToMany(mappedBy = "model", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<ModelField> fields;

    @OneToMany(mappedBy = "model", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<ModelEndpoint> endpoints;
    /**
     * 元数据
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @PrimaryKeyJoinColumn
    private ModelMetadata metadata;

    public static class ModelBuilder {
        private List<ModelFeature> features;

        public ModelBuilder features(String... features) {
            this.features = Arrays.stream(features).map(id -> ModelFeature.builder().id(id).build()).collect(Collectors.toList());
            return this;
        }
    }

    @Override
    public String toString() {
        return "Model{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", dataSource='" + dataSource + '\'' +
            ", isSystem=" + isSystem +
            ", labels=" + Arrays.toString(labels) +
            ", features=" + features +
            ", fields=" + fields +
            '}';
    }
}
