package cn.asany.im.auth.service.vo;

import cn.asany.im.utils.GeneralRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ParseTokenRequestBody extends GeneralRequest {}
