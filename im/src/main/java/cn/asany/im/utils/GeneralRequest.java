package cn.asany.im.utils;

import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import net.asany.jfantasy.framework.util.common.StringUtil;

@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@MappedSuperclass
public abstract class GeneralRequest {
  @Builder.Default protected String operationID = StringUtil.uuid();
}
