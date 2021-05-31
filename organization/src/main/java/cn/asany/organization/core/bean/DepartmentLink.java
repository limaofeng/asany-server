package cn.asany.organization.core.bean;

import cn.asany.organization.core.bean.enums.DepartmentLinkType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

/**
 * @author limaofeng
 * @version V1.0
 * @Description: 部门关联表
 * @date 2019-05-27 12:56
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORG_DEPARTMENT_LINK")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DepartmentLink extends BaseBusEntity {
    @Id
    @Column(name = "ID", precision = 22)
    @GeneratedValue(generator = "org_department_link_gen")
    @TableGenerator(name = "org_department_link_gen", table = "sys_sequence", pkColumnName = "gen_name", pkColumnValue = "org_department_link:id", valueColumnName = "gen_value")
    private Long id;
    /**
     * 内部部门
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENT_ID", foreignKey = @ForeignKey(name = "FK_DEPARTMENT_LINK_EID"))
    private Department department;
    /**
     * 链接类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 10)
    private DepartmentLinkType type;
    /**
     * 外部ID
     */
    @Column(name = "LINK_ID", length = 32)
    private String linkId;

}
