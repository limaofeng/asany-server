package cn.asany.im.auth.service.vo;

import cn.asany.im.utils.GeneralResponse;
import lombok.*;

@Data
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class UserRegisterResponseBody extends GeneralResponse<UserRegisterData> {}
