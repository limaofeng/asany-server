package cn.asany.base.common.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Owner<T extends Enum<T>> {
  /** 所有者ID */
  @Column(name = "OWNER_ID", length = 30)
  private String id;
  /** 所有者类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "OWNER_TYPE", length = 20)
  private T type;
  /** 所有者名称 */
  @Column(name = "OWNER_NAME", length = 50)
  private String name;
}
