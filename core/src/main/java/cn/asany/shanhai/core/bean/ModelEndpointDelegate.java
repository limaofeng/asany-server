package cn.asany.shanhai.core.bean;

import cn.asany.shanhai.core.bean.enums.ModelEndpointDelegateType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

/**
 * 实体接口委派
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@Entity
@Table(name = "SH_MODEL_ENDPOINT_DELEGATE")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ModelEndpointDelegate extends BaseBusEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 委托类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 50)
    private ModelEndpointDelegateType type;
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
     * 规则
     */
    @Transient
    private ModelEndpointDelegateRule rules;
    /**
     * 服务
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SERVICE_ID", foreignKey = @ForeignKey(name = "FK_MODEL_ENDPOINT_DELEGATE_SID"), nullable = false)
    private NameServer nameServer;

    static class ModelEndpointDelegateRule {
        String query;
        Object reject;
        String args;
    }
}
