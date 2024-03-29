package cn.asany.pm.screen.bean.screenbind;

import cn.asany.pm.screen.bean.IssueScreen;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import org.jfantasy.framework.util.common.StringUtil;

/**
 * @author limaofeng@msn.com @ClassName: ProjectTaskDeserializer @Description: (这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
public class ScreenDeserializer extends JsonDeserializer<IssueScreen> {
  @Override
  public IssueScreen deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    String value = jp.getValueAsString();
    if (StringUtil.isBlank(value)) {
      return null;
    }
    return IssueScreen.builder().id(Long.valueOf(value)).build();
  }
}
