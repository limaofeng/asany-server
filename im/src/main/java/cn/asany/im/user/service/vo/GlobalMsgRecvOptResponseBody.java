package cn.asany.im.user.service.vo;

import cn.asany.im.utils.GeneralResponse;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GlobalMsgRecvOptResponseBody extends GeneralResponse<String> {}
