package cn.asany.pm.rule.bean.databind;

import cn.asany.pm.rule.bean.IssueAppraisalRuleInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class IssueAppraisalRuleInfoDeserializer extends JsonDeserializer<IssueAppraisalRuleInfo> {
  @Override
  public IssueAppraisalRuleInfo deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException {
    Long value = jp.getValueAsLong();
    if (value == null) {
      return null;
    }
    return IssueAppraisalRuleInfo.builder().id(value).build();
  }
}
