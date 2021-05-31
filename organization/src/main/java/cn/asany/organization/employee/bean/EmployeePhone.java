package cn.asany.organization.employee.bean;

import cn.asany.organization.employee.bean.enums.PhoneStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

/**
 * @author limaofeng
 * @version V1.0
 * @Description: 员工
 * @date 2019-03-11 14:19
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORG_EMPLOYEE_PHONE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "employeePositions", "links"})
public class EmployeePhone extends BaseBusEntity {

    @Id
    @Column(name = "ID", precision = 22)
    @GeneratedValue(generator = "org_employee_phone_gen")
    @TableGenerator(name = "org_employee_phone_gen", table = "sys_sequence", pkColumnName = "gen_name", pkColumnValue = "org_employee_phone:id", valueColumnName = "gen_value")
    private Long id;

    @Column(name = "IS_PRIMARY", nullable = false)
    private Boolean primary;

    @Column(name = "LABEL", length = 30)
    private String label;
    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private PhoneStatus status;
    /**
     * 电话
     */
    @Column(name = "phone", nullable = false, length = 25)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID", foreignKey = @ForeignKey(name = "ORG_EMPLOYEE_PHONE_EID"), nullable = false, updatable = false)
    private Employee employee;
}
