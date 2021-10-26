package cn.asany.workflow.field.bean;

import cn.asany.workflow.core.bean.WorkflowInstance;
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
 * @date 2019-05-24 16:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FSM_FIELD_VALUE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FieldValue extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  private Long id;
  /** 对应的问题 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "INSTANCE", foreignKey = @ForeignKey(name = "FK_FIELD_VALUE_INSTANCE"))
  private WorkflowInstance instance;
  /** 对应的字段 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "FIELD", foreignKey = @ForeignKey(name = "FK_FIELD_VALUE_FIELD"))
  private Field field;
  /** 存储的值 */
  @Column(name = "VALUE", length = 250)
  private String value;
}
