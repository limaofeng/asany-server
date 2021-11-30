package cn.asany.nuwa.app.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 组件
 *
 * @author limaofeng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NuwaComponent {
  private String template;
  private String blocks;

  public NuwaComponent(String template) {
    this.template = template;
  }
}
