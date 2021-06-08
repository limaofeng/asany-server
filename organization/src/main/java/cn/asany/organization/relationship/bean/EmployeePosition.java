package cn.asany.organization.relationship.bean;

import cn.asany.organization.core.bean.Department;
import cn.asany.organization.core.bean.Organization;
import cn.asany.organization.core.bean.databind.DepartmentDeserializer;
import cn.asany.organization.core.bean.databind.DepartmentSerializer;
import cn.asany.organization.employee.bean.Employee;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 员工职位
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-04-04 20:00
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORG_EMPLOYEE_POSITION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EmployeePosition extends BaseBusEntity {

    @Id
    @Column(name = "ID", precision = 22)
    @GeneratedValue(generator = "org_employee_position_gen")
    @TableGenerator(name = "org_employee_position_gen", table = "sys_sequence", pkColumnName = "gen_name", pkColumnValue = "org_employee_position:id", valueColumnName = "gen_value")
    private Long id;
    /**
     * 是否为主部门
     */
    @Column(name = "IS_PRIMARY")
    private Boolean primary;
    /**
     * 员工
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID", foreignKey = @ForeignKey(name = "ORG_EMPLOYEE_POSITION_EID"), nullable = false, updatable = false)
    private Employee employee;
    /**
     * 员工所在部门的职位
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POSITION_ID", foreignKey = @ForeignKey(name = "ORG_EMPLOYEE_POSITION_PID"))
    private Position position;
    /**
     * 所在部门
     */
    @JsonSerialize(using = DepartmentSerializer.class)
    @JsonDeserialize(using = DepartmentDeserializer.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENT_ID", foreignKey = @ForeignKey(name = "ORG_EMPLOYEE_POSITION_DID"), nullable = false)
    private Department department;
    /**
     * 所属组织
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORGANIZATION_ID", foreignKey = @ForeignKey(name = "FK_EMPLOYEE_POSITION_OID"), updatable = false, nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORGANIZATION_EMPLOYEE_ID", foreignKey = @ForeignKey(name = "FK_ORGANIZATION_EMPLOYEE_TID"), updatable = false, nullable = false)
    private OrganizationEmployee status;

    @JsonIgnore
    public Set<String> getAuthoritys() {
        Set<String> authoritys = new HashSet<>();
        authoritys.addAll(this.getDepartment().getAuthoritys());
//        authoritys.add(SecurityScope.newInstance(SecurityType.organization, this.organization.getId()).toString());
//        if (this.position != null) {
//            authoritys.add(SecurityScope.newInstance(SecurityType.position, this.position.getId().toString()).toString());
//            authoritys.add(SecurityScope.newInstance(SecurityType.job, this.position.getJob().getId().toString()).toString());
//        }
        return authoritys;
    }


}