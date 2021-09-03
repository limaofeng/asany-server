package cn.asany.cms.article.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 为 收藏 / 点赞 等功能提供统一支持
 *
 * @author limaofeng
 * @version V1.0 @Description:
 * @date 2019-08-14 09:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "ORG_STAR",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "UK_ORG_STAR",
          columnNames = {"TYPE", "VALUE", "EMPLOYEE_ID"})
    })
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Star extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, length = 20)
  @GeneratedValue(generator = "star_gen")
  @TableGenerator(
      name = "star_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "org_star:id",
      valueColumnName = "gen_value")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "TYPE", updatable = false, nullable = false)
  private StarType starType;

  @Column(name = "VALUE", updatable = false, nullable = false, length = 50)
  private String galaxy;

  @Column(
      name = "EMPLOYEE_ID",
      nullable = false /*, foreignKey = @ForeignKey(name = "FK_STAR_EMPLOYEE")*/)
  private String stargazer;
}
