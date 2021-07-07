package cn.asany.shanhai.core.bean;

import cn.asany.shanhai.core.bean.enums.ModelEndpointType;
import cn.asany.shanhai.core.support.graphql.resolvers.DelegateDataFetcher;
import cn.asany.shanhai.core.utils.ModelUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.spring.SpringContextUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(of = "id")
@Entity
@Table(name = "SH_MODEL_ENDPOINT", uniqueConstraints = @UniqueConstraint(columnNames = {"MODEL_ID", "TYPE", "CODE"}, name = "UK_MODEL_ENDPOINT_KEY"))
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
    @JoinColumn(name = "MODEL_ID", foreignKey = @ForeignKey(name = "FK_MODEL_ENDPOINT_MID"), nullable = false)
    private Model model;
    /**
     * 参数
     */
    @OneToMany(mappedBy = "endpoint", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<ModelEndpointArgument> arguments;
    /**
     * 返回类型
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @PrimaryKeyJoinColumn
    private ModelEndpointReturnType returnType;

    /**
     * 委派
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DELEGATE_ID", foreignKey = @ForeignKey(name = "FK_MODEL_ENDPOINT_DID"))
    private ModelDelegate delegate;

    public static class ModelEndpointBuilder {

        public ModelEndpointBuilder returnType(Model type) {
            this.returnType = ModelEndpointReturnType.builder().type(type).build();
            return this;
        }

        public ModelEndpointBuilder returnType(Boolean multiple, Model type) {
            this.returnType = ModelEndpointReturnType.builder().list(multiple).type(type).build();
            return this;
        }

        public ModelEndpointBuilder returnType(String type) {
            this.returnType = ModelEndpointReturnType.builder().type(Model.builder().code(type).build()).build();
            return this;
        }

        public ModelEndpointBuilder returnType(Boolean multiple, String type) {
            this.returnType = ModelEndpointReturnType.builder().list(multiple).type(Model.builder().code(type).build()).build();
            return this;
        }

        public ModelEndpointBuilder returnType(Boolean required, Boolean multiple, String type) {
            this.returnType = ModelEndpointReturnType.builder().required(required).list(multiple).type(Model.builder().code(type).build()).build();
            return this;
        }

        public ModelEndpointBuilder argument(String name, String type) {
            if (this.arguments == null) {
                this.arguments = new ArrayList<>();
            }
            this.arguments.add(ModelEndpointArgument.builder().name(name).type(type).build());
            return this;
        }

        public ModelEndpointBuilder argument(String name, Model type, String description) {
            if (this.arguments == null) {
                this.arguments = new ArrayList<>();
            }
            this.arguments.add(ModelEndpointArgument.builder().name(name).type(type).description(description).build());
            return this;
        }

        public ModelEndpointBuilder argument(String name, String type, Boolean required) {
            if (this.arguments == null) {
                this.arguments = new ArrayList<>();
            }
            this.arguments.add(ModelEndpointArgument.builder().name(name).required(required).type(type).build());
            return this;
        }

        public ModelEndpointBuilder argument(String name, String type, String description) {
            if (this.arguments == null) {
                this.arguments = new ArrayList<>();
            }
            this.arguments.add(ModelEndpointArgument.builder().name(name).description(description).type(type).build());
            return this;
        }

        public ModelEndpointBuilder argument(String name, String type, String description, Object defaultValue) {
            if (this.arguments == null) {
                this.arguments = new ArrayList<>();
            }
            this.arguments.add(ModelEndpointArgument.builder().name(name).description(description).type(type).build());
            return this;
        }

        public ModelEndpointBuilder argument(String name, Model type, String description, Object defaultValue) {
            if (this.arguments == null) {
                this.arguments = new ArrayList<>();
            }
            this.arguments.add(ModelEndpointArgument.builder().name(name).description(description).type(type).build());
            return this;
        }

        public ModelEndpointBuilder argument(String name, String type, Boolean required, String description, Object defaultValue) {
            if (this.arguments == null) {
                this.arguments = new ArrayList<>();
            }
            this.arguments.add(ModelEndpointArgument.builder().name(name).required(required).description(description).type(type).build());
            return this;
        }

        public ModelEndpointBuilder delegate(Class<? extends DelegateDataFetcher> resolverClass) {
            ModelUtils modelUtils = SpringContextUtil.getBeanByType(ModelUtils.class);
            this.delegate = modelUtils.getDelegate(resolverClass);
            return this;
        }
    }
}
