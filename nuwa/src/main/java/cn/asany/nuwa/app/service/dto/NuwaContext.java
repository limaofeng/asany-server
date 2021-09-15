package cn.asany.nuwa.app.service.dto;

import cn.asany.nuwa.app.bean.Routespace;
import lombok.Builder;
import lombok.Data;

/**
 * 上下文对象
 *
 * @author limaofeng
 */
@Data
@Builder
public class NuwaContext {
  @Builder.Default private int level = 1;
  private Routespace routespace;
}
