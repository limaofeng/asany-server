package cn.asany.organization.employee.bean;

import cn.asany.base.common.bean.PhoneNumber;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

/**
 * 员工手机
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-03-11 14:19
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORG_EMPLOYEE_PHONE_NUMBER")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "employeePositions", "links"})
public class EmployeePhoneNumber extends BaseBusEntity {

    @Id
    @Column(name = "ID", precision = 22)
    @GeneratedValue(generator = "org_employee_phone_gen")
    @TableGenerator(name = "org_employee_phone_gen", table = "sys_sequence", pkColumnName = "gen_name", pkColumnValue = "org_employee_phone:id", valueColumnName = "gen_value")
    private Long id;
    /**
     * 主号码
     */
    @Column(name = "IS_PRIMARY", nullable = false)
    private Boolean primary;
    /**
     * 标签
     */
    @Column(name = "LABEL", length = 30)
    private String label;
    /**
     * 电话
     */
    @Embedded
    private PhoneNumber phone;
    /**
     * 员工
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID", foreignKey = @ForeignKey(name = "ORG_EMPLOYEE_PHONE_EID"), nullable = false, updatable = false)
    private Employee employee;
}
