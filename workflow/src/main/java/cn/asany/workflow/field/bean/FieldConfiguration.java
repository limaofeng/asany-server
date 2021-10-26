package cn.asany.workflow.field.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-05-22 14:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FSM_FIELD_CONFIGURATION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FieldConfiguration extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @GeneratedValue(generator = "gd_field_configuration_gen")
  @TableGenerator(
      name = "gd_field_configuration_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "gd_field_configuration:id",
      valueColumnName = "gen_value")
  private Long id;

  @Column(name = "NAME", length = 50, nullable = false)
  private String name;

  @Column(name = "DESCRIPTION", length = 250)
  private String description;

  @OneToMany(
      mappedBy = "fieldConfiguration",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  private List<FieldConfigurationItem> fields;
}
