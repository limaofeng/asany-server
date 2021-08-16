package cn.asany.security.core.graphql.inputs;

import cn.asany.security.core.bean.enums.PsdSendType;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;

/**
 * @description:
 * @author: Poison
 * @time: 2020/9/28 2:42 PM
 */
@Data
public class SendConfigureFilter {
  private PropertyFilterBuilder builder = new PropertyFilterBuilder();

  public void setAppId(String appId) {
    if (StringUtils.isNotBlank(appId)) {
      builder.equal("appId", appId);
    }
  }

  public void setType(PsdSendType type) {
    if (type != null) {
      builder.equal("type", type);
    }
  }
}
