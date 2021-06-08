package cn.asany.organization.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

/**
 * 职务
 *
 * @author limaofeng
 * @version V1.0
 * @date 2019-05-10 08:59
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORG_JOB")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Job extends BaseBusEntity {

    private static final long serialVersionUID = -7020427994563623645L;

    @Id
    @Column(name = "ID", precision = 22)
    @GeneratedValue(generator = "org_job_gen")
    @TableGenerator(name = "org_job_gen", table = "sys_sequence", pkColumnName = "gen_name", pkColumnValue = "org_job:id", valueColumnName = "gen_value")
    private Long id;
    /**
     * 职务名称
     */
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;
    /**
     * 职务描述信息
     */
    @Column(name = "DESCRIPTION", length = 250)
    private String description;
    /**
     * 职务所属组织
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORGANIZATION_ID", foreignKey = @ForeignKey(name = "FK_ORG_JOB_OID"), updatable = false, nullable = false)
    private Organization organization;

    /**
     * 职务级别
     */
    @Column(name = "level",length = 10)
    private Long level;
}