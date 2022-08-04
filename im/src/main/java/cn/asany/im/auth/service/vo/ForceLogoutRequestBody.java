package cn.asany.im.auth.service.vo;

import cn.asany.im.utils.GeneralRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ForceLogoutRequestBody extends GeneralRequest {
  private String fromUserID;
  private Integer platform;
}
