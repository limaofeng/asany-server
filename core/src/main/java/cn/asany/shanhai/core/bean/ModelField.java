package cn.asany.shanhai.core.bean;

import cn.asany.shanhai.core.utils.ModelUtils;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.spring.SpringContextUtil;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "SH_MODEL_FIELD", uniqueConstraints = @UniqueConstraint(columnNames = {"MODEL_ID", "CODE"}, name = "UK_MODEL_FIELD_CODE"))
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
     * 编码 用于 HQL 名称及 API 名称
     */
    @Column(name = "CODE", length = 50)
    private String code;
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
    @Builder.Default
    @Column(name = "IS_REQUIRED")
    private Boolean required = false;
    /**
     * 是否主键
     */
    @Builder.Default
    @Column(name = "IS_PRIMARY_KEY", length = 10, nullable = false)
    private Boolean primaryKey = false;
    /**
     * 字段类型
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TYPE_ID", foreignKey = @ForeignKey(name = "FK_MODEL_FIELD_TID"), nullable = false)
    private Model type;
    /**
     * 是否唯一
     */
    @Builder.Default
    @Column(name = "IS_UNIQUE", length = 1)
    private Boolean unique = false;
    /**
     * 存储值为列表，而不是单个值
     */
    @Builder.Default
    @Column(name = "IS_LIST", length = 1)
    private Boolean list = false;
    /**
     * 是否系统字段
     */
    @Builder.Default
    @Column(name = "IS_SYSTEM", length = 1)
    private Boolean system = false;

    /**
     * 序号
     */
    @Column(name = "SORT")
    private Integer sort;
    /**
     * 实体
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODEL_ID", foreignKey = @ForeignKey(name = "FK_MODEL_FIELD_MODEL_ID"), nullable = false)
    private Model model;
    /**
     * 元数据
     */
    @OneToOne(mappedBy = "field", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private ModelFieldMetadata metadata;
    /**
     * 委派
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DELEGATE_ID", foreignKey = @ForeignKey(name = "FK_MODEL_FIELD_DID"))
    private ModelEndpointDelegate delegate;

    public static class ModelFieldBuilder {
        private ModelFieldMetadata metadata;

        public ModelFieldBuilder metadata(String columnName) {
            this.metadata = ModelFieldMetadata.builder().databaseColumnName(columnName).build();
            return this;
        }

        public ModelFieldBuilder type(String id) {
            ModelUtils modelUtils = SpringContextUtil.getBeanByType(ModelUtils.class);
            this.type = modelUtils.getModelByCode(id);
            return this;
        }

        public ModelFieldBuilder type(Model type) {
            this.type = type;
            return this;
        }
    }

    @Override
    public String toString() {
        return "ModelField{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", defaultValue='" + defaultValue + '\'' +
            ", required=" + required +
            ", primaryKey=" + primaryKey +
            ", type='" + type + '\'' +
            ", unique=" + unique +
            ", system=" + system +
            ", list=" + list +
            ", sort=" + sort +
            '}';
    }
}
