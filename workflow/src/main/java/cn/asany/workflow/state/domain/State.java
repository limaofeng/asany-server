package cn.asany.workflow.state.domain;

import cn.asany.workflow.state.domain.enums.StateCategory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 流程状态
 *
 * @author limaofeng
 * @date 2019/5/7
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FSM_STATE")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class State extends BaseBusEntity {

  @Id
  @Column(name = "ID", length = 22)
  private Long id;
  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;
  /** 描述 */
  @Column(name = "DESCRIPTION", length = 200)
  private String description;
  /** 状态排序字段 */
  @Column(name = "SORT")
  private Integer sort;
  /** 阶段 */
  @Enumerated(EnumType.STRING)
  @Column(name = "CATEGORY", length = 20)
  private StateCategory category;
  /** 是否启用 */
  @Column(name = "ENABLED")
  private Boolean enabled;
}
