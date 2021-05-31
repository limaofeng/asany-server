package cn.asany.organization.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

/**
 * 组织状态
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019/11/13 11:36 上午
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORG_ORGANIZATION_EMPLOYEE_STATUS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OrganizationEmployeeStatus extends BaseBusEntity {
    /**
     * ID
     */
    @Id
    @Column(name = "ID", length = 20)
    @GeneratedValue(generator = "org_organization_employee_status_gen")
    @TableGenerator(name = "org_organization_employee_status_gen", table = "sys_sequence", pkColumnName = "gen_name", pkColumnValue = "org_organization_employee_status:id", valueColumnName = "gen_value")
    private Long id;

    @Column(name = "NAME", length = 255)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORGANIZATION_ID", foreignKey = @ForeignKey(name = "FK_ORGANIZATION_EMPLOYEE_STATUS_OID"), updatable = false, nullable = false)
    private Organization organization;

    /**
     * 编码
     */
    @Column(name = "CODE", length = 255)
    private String code;
    /**
     * 是否是默认值
     */
    @Column(name = "is_default", length = 10)
    private Boolean isDefault;


}
