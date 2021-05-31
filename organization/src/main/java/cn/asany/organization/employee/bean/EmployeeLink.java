package cn.asany.organization.employee.bean;

import cn.asany.organization.core.bean.enums.LinkType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

/**
 * @author limaofeng
 * @version V1.0
 * @Description: TODO
 * @date 2019-05-27 12:56
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORG_EMPLOYEE_LINK")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EmployeeLink extends BaseBusEntity {
    @Id
    @Column(name = "ID", precision = 22)
    @GeneratedValue(generator = "org_employee_link_gen")
    @TableGenerator(name = "org_employee_link_gen", table = "sys_sequence", pkColumnName = "gen_name", pkColumnValue = "org_employee_link:id", valueColumnName = "gen_value")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID", foreignKey = @ForeignKey(name = "FK_EMPLOYEE_LINK_EID"))
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 10)
    private LinkType type;

    @Column(name = "LINK_ID", length = 32)
    private String linkId;

}
