package cn.asany.workflow.field.bean;

import cn.asany.workflow.field.bean.enums.FieldCategory;
import cn.asany.workflow.field.bean.enums.FieldType;
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
 * @date 2019-05-22 14:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FSM_FIELD")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Field extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  private Long id;

  @Column(name = "NAME", length = 50, nullable = false)
  private String name;

  @Column(name = "LABEL", length = 50, nullable = false)
  private String label;

  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20, nullable = false)
  private FieldType type;

  @Enumerated(EnumType.STRING)
  @Column(name = "CATEGORY", length = 20, nullable = false)
  private FieldCategory category;

  @Column(name = "RENDERER", length = 20)
  private String renderer;
}
