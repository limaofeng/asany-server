package cn.asany.im.auth.service.vo;

import cn.asany.im.utils.GeneralResponse;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
public class UserTokenResponseBody extends GeneralResponse<UserTokenData> {}
