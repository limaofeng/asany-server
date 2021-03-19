package cn.asany.shanhai.core.bean;

import cn.asany.shanhai.core.bean.enums.ModelEndpointType;
import cn.asany.shanhai.core.utils.ModelUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.spring.SpringContextUtil;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "SH_MODEL_ENDPOINT")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ModelEndpoint extends BaseBusEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 名称
     */
    @Column(name = "CODE", length = 100, nullable = false)
    private String code;
    /**
     * 名称
     */
    @Column(name = "NAME", length = 100, nullable = false)
    private String name;
    /**
     * 类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 10, nullable = false)
    private ModelEndpointType type;
    /**
     * 描述
     */
    @Column(name = "DESCRIPTION", length = 200)
    private String description;
    /**
     * 实体
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODEL_ID", foreignKey = @ForeignKey(name = "FK_SH_MODEL_ENDPOINT_MID"), nullable = false)
    private Model model;
    /**
     * 参数
     */
    @OneToMany(mappedBy = "endpoint", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<ModelEndpointArgument> arguments;
    /**
     * 返回类型
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @PrimaryKeyJoinColumn
    private ModelEndpointReturnType returnType;

    public static class ModelEndpointBuilder {

        public ModelEndpointBuilder returnType(Model type) {
            this.returnType = ModelEndpointReturnType.builder().type(type).build();
            return this;
        }

        public ModelEndpointBuilder returnType(String type) {
            ModelUtils modelUtils = SpringContextUtil.getBeanByType(ModelUtils.class);
            this.returnType = ModelEndpointReturnType.builder().type(modelUtils.getModelByCode(type)).build();
            return this;
        }

        public ModelEndpointBuilder returnType(Boolean multiple, String type) {
            ModelUtils modelUtils = SpringContextUtil.getBeanByType(ModelUtils.class);
            this.returnType = ModelEndpointReturnType.builder().isList(multiple).type(modelUtils.getModelByCode(type)).build();
            return this;
        }

        public ModelEndpointBuilder returnType(Boolean required, Boolean multiple, String type) {
            ModelUtils modelUtils = SpringContextUtil.getBeanByType(ModelUtils.class);
            this.returnType = ModelEndpointReturnType.builder().isRequired(required).isList(multiple).type(modelUtils.getModelByCode(type)).build();
            return this;
        }

        public ModelEndpointBuilder arguments(ModelEndpointArgument... arguments) {
            this.arguments = Arrays.asList(arguments);
            return this;
        }
    }
}
