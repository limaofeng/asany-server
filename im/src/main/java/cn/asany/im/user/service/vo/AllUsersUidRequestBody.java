package cn.asany.im.user.service.vo;

import cn.asany.im.utils.GeneralRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class AllUsersUidRequestBody extends GeneralRequest {}
