package cn.asany.workflow.screen.domain;

import cn.asany.workflow.field.domain.FieldConfigurationItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.asany.jfantasy.framework.dao.BaseBusEntity;

/**
 * 将字段分配给页面
 *
 * @author limaofeng
 * @date 2022/7/28 9:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FSM_FIELD_SCREEN")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class FieldToScreen extends BaseBusEntity {

  @Id
  @Column(name = "ID", length = 22)
  private Long id;

  /** 页面 */
  @JsonProperty("screen")
  @JoinColumn(name = "SCREEN", foreignKey = @ForeignKey(name = "FK_GD_FIELD_SCREEN_SID"))
  @ManyToOne(fetch = FetchType.LAZY)
  private Screen screen;

  /** 字段 */
  @JoinColumn(name = "FIELD_ITEM_ID", foreignKey = @ForeignKey(name = "FK_GD_FIELD_SCREEN_FID"))
  @ManyToOne(fetch = FetchType.LAZY)
  private FieldConfigurationItem field;
  /** TabPane */
  @JoinColumn(name = "TAB_PANE", foreignKey = @ForeignKey(name = "FK_GD_FIDEL_TAB_PANE_TID"))
  @ManyToOne(fetch = FetchType.LAZY)
  private ScreenTabPane tabPane;
  /** 排序字段 */
  @Column(name = "SORT", length = 22)
  private Long order;
}
