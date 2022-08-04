package cn.asany.im.user.service.vo;

import cn.asany.im.utils.GeneralResponse;
import java.util.List;
import lombok.*;

@Data
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AccountCheckResponseBody extends GeneralResponse<List<AccountCheckData>> {}
