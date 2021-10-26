package cn.asany.workflow.field.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-05-22 14:08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FSM_FIELD_CONFIGURATION_ITEM")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FieldConfigurationItem extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "gd_field_configuration_item_gen")
  @TableGenerator(
      name = "gd_field_configuration_item_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "gd_field_configuration_item:id",
      valueColumnName = "gen_value")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "FIELD_ID",
      foreignKey = @ForeignKey(name = "FK_FIELD_CONFIGURATION_ITEM_FIELD_ID"))
  private Field field;

  @Column(name = "REQUIRED")
  private Boolean required;

  @Column(name = "RENDERER", length = 32)
  private String renderer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "CONFIG_ID",
      foreignKey = @ForeignKey(name = "FK_FIELD_CONFIGURATION_ITEM_CONFIG_ID"))
  private FieldConfiguration fieldConfiguration;
}
