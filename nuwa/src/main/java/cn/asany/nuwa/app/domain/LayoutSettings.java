package cn.asany.nuwa.app.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 布局设置
 *
 * @author limaofeng
 */
@Data
@Embeddable
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LayoutSettings implements Serializable {
  /** 不使用默认布局 */
  @Builder.Default
  @Column(name = "PURE", length = 1)
  private Boolean pure = false;
  /** 隐藏菜单栏 */
  @Builder.Default
  @Column(name = "HIDE_MENU", length = 1)
  private Boolean hideMenu = false;
}
