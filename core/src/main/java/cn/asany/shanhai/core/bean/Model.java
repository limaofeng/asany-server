package cn.asany.shanhai.core.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.dao.hibernate.converter.StringArrayConverter;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

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
//    /**
//     * 状态：草稿、发布
//     */
//    @Enumerated(EnumType.STRING)
//    @Column(name = "STATUS", length = 20)
//    private ModelStatus status;
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

    @Convert(converter = StringArrayConverter.class)
    @Column(name = "FEATURES", columnDefinition = "JSON")
    private String[] features;

    @OrderBy("sort asc ")
    @OneToMany(mappedBy = "model", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<ModelField> fields;
    /**
     * 元数据
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @PrimaryKeyJoinColumn
    private ModelMetadata metadata;

    @Override
    public String toString() {
        return "Model{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", dataSource='" + dataSource + '\'' +
            ", isSystem=" + isSystem +
            ", labels=" + Arrays.toString(labels) +
            ", features=" + Arrays.toString(features) +
            ", fields=" + fields +
            '}';
    }
}
