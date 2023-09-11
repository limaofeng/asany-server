package cn.asany.nuwa.app.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 组件
 *
 * @author limaofeng
 */
@Data
@NoArgsConstructor
public class NuwaComponent {
  private String template;
  private String blocks;

  public NuwaComponent(String template) {
    this.template = template;
  }

  public NuwaComponent(String template, String blocks) {
    this.template = template;
    this.blocks = blocks;
  }
}
