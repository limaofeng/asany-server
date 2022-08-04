package cn.asany.im.user.service.vo;

import cn.asany.im.utils.GeneralRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class GlobalMsgRecvOptRequestBody extends GeneralRequest {
  /** 为全局免打扰设置0为关闭 1为开启 */
  private Integer globalRecvMsgOpt;
}
