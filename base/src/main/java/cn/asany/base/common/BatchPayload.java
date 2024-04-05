package cn.asany.base.common;

import lombok.Data;

@Data
public class BatchPayload {
  private Long count;

  public static BatchPayload of(Long count) {
    BatchPayload payload = new BatchPayload();
    payload.setCount(count);
    return payload;
  }

  public static BatchPayload of(Integer count) {
    BatchPayload payload = new BatchPayload();
    payload.setCount(count.longValue());
    return payload;
  }
}
