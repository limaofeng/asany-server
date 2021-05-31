package cn.asany.organization.employee.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 员工地址信息
 *
 * @author limaofeng
 * @version V1.0
 * @Description: TODO
 * @date 2019/11/1 3:20 下午
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORG_EMPLOYEE_ADDRESS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "employee"})
public class EmployeeAddress implements Serializable {
    @Id
    @Column(name = "ID", precision = 22)
    @GeneratedValue(generator = "org_employee_address_gen")
    @TableGenerator(name = "org_employee_address_gen", table = "sys_sequence", pkColumnName = "gen_name", pkColumnValue = "org_employee_address:id", valueColumnName = "gen_value")
    private Long id;

    @Column(name = "IS_PRIMARY", nullable = false)
    private Boolean primary;

    @Column(name = "LABEL", length = 30)
    private String label;

    /**
     * 国家
     */
    @Column(name = "COUNTRY", length = 30)
    private String country;
    /**
     * 省
     */
    @Column(name = "PROVINCE", length = 30)
    private String province;
    /**
     * 城市
     */
    @Column(name = "CITY", length = 30)
    private String city;
    /**
     * 区
     */
    @Column(name = "DISTRICT", length = 30)
    private String district;
    /**
     * 街道
     */
    @Column(name = "STREET", length = 30)
    private String street;
    /**
     * 邮编
     */
    @Column(name = "POSTAL_CODE", length = 10)
    private String postalCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID", foreignKey = @ForeignKey(name = "ORG_EMPLOYEE_ADDRESS_EID"), nullable = false, updatable = false)
    private Employee employee;
}
