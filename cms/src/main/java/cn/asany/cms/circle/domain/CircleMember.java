package cn.asany.cms.circle.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * @author admin
 * @date 2017/7/17
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "CMS_CIRCLE_MEMBER",
    uniqueConstraints =
        @UniqueConstraint(
            name = "UK_CIRCLE_MEMBER",
            columnNames = {"CIRCLE_ID", "USER_ID"}))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CircleMember extends BaseBusEntity {
  private static final long serialVersionUID = 5943657998408920023L;

  @Id
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "circle_gen")
  @TableGenerator(
      name = "circle_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "cms_circle:id",
      valueColumnName = "gen_value")
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "CIRCLE_ID", nullable = false)
  private Long category;

  @Column(name = "USER_ID", length = 32)
  private Long userId;
}
