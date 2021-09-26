package cn.asany.nuwa.app.bean;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * 布局设置
 *
 * @author limaofeng
 */
@Data
@SuperBuilder
@Embeddable
public class LayoutSettings {
  /** 不使用默认布局 */
  @Builder.Default
  @Column(name = "PURE", length = 20)
  private Boolean pure = false;

  public LayoutSettings() {
    this.pure = true;
  }

  public LayoutSettings(String pure) {
    this.pure = !Boolean.parseBoolean(pure);
  }
}
