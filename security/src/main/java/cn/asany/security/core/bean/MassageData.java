package cn.asany.security.core.bean;

import lombok.Builder;
import lombok.Data;

/**
 * @description:
 * @author: Poison
 * @time: 2020/9/23 3:12 PM
 */
@Data
@Builder
public class MassageData {
  private String key;
  private Object value;
}
