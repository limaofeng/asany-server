package cn.asany.organization.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 为 收藏 / 点赞 等功能提供统一支持
 *
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
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
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "TYPE",
      updatable = false,
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_STAR_TYPE"))
  private StarType starType;

  @Column(name = "VALUE", updatable = false, nullable = false, length = 50)
  private String galaxy;

  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REFRESH})
  @JoinColumn(
      name = "EMPLOYEE_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_STAR_EMPLOYEE"))
  private Employee stargazer;

  /** 阅读时长 */
  @Column(name = "READING_TIME")
  private Long readingTime;
}
