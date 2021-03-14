package cn.asany.shanhai.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "SH_MODEL_FIELD")
public class ModelField extends BaseBusEntity {
    /**
     * id主键
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "DDM_MODEL_FIELD")
    @TableGenerator(name = "DDM_MODEL_FIELD", table = "sys_sequence", pkColumnName = "gen_name", pkColumnValue = "MODEL_FIELD:id", valueColumnName = "gen_value")
    private Long id;
    /**
     * 名称
     */
    @Column(name = "NAME", length = 50)
    private String name;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION", length = 200)
    private String description;
    /**
     * 默认值
     */
    @Column(name = "DEFAULT_VALUE", length = 100)
    private String defaultValue;
    /**
     * 是否必填
     */
    @Column(name = "IS_REQUIRED")
    private Boolean isRequired;
    /**
     * 是否主键
     */
    @Column(name = "IS_PRIMARY_KEY", length = 10, nullable = false)
    private Boolean isPrimaryKey;
    /**
     * 字段类型
     */
    @Column(name = "FIELD_TYPE", length = 50, nullable = false)
    private String type;
    /**
     * 是否唯一
     */
    @Column(name = "IS_UNIQUE", length = 1)
    private Boolean isUnique;
    /**
     * 存储值为列表，而不是单个值
     */
    @Column(name = "IS_LIST", length = 1)
    private Boolean isList;
    /**
     * 序号
     */
    @Column(name = "SORT")
    private Integer sort;
    /**
     * 实体
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODEL_ID", foreignKey = @ForeignKey(name = "SH_MODEL_FIELD_MODEL_ID"), nullable = false)
    private Model model;
    /**
     * 元数据
     */
    @OneToOne(mappedBy = "field", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private ModelFieldMetadata metadata;

    @Override
    public String toString() {
        return "ModelField{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", defaultValue='" + defaultValue + '\'' +
            ", isRequired=" + isRequired +
            ", isPrimaryKey=" + isPrimaryKey +
            ", type='" + type + '\'' +
            ", isUnique=" + isUnique +
            ", isList=" + isList +
            ", sort=" + sort +
            '}';
    }
}
