package cn.asany.organization.employee.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

/**
 * 为 收藏 / 点赞 等功能提供统一支持
 * @author limaofeng
 * @version V1.0
 * @Description: TODO
 * @date 2019-08-14 09:43
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORG_STAR", uniqueConstraints = {@UniqueConstraint(name = "UK_ORG_STAR", columnNames = {"TYPE", "VALUE", "EMPLOYEE_ID"})})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Star extends BaseBusEntity {

    @Id
    @Column(name = "ID", nullable = false, updatable = false, length = 20)
    @GeneratedValue(generator = "star_gen")
    @TableGenerator(name = "star_gen", table = "sys_sequence", pkColumnName = "gen_name", pkColumnValue = "org_star:id", valueColumnName = "gen_value")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TYPE", updatable = false, nullable = false)
    private StarType starType;
    @Column(name = "VALUE", updatable = false, nullable = false, length = 50)
    private String galaxy;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "EMPLOYEE_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_STAR_EMPLOYEE"))
    private Employee stargazer;

    /**
     * 阅读时长
     */
    @Column(name = "READING_TIME")
    private Long readingTime;
}
