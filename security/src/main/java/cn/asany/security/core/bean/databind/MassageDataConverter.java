package cn.asany.security.core.bean.databind;

import cn.asany.security.core.bean.MassageData;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.ListUtils;
import org.jfantasy.framework.util.common.StringUtil;

/**
 * @author liumeng @Description: (这里用一句话描述这个类的作用)
 * @date 11:35 2020-04-23
 */
public class MassageDataConverter extends JsonDeserializer<List<MassageData>> {
  @Override
  public List<MassageData> deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException {
    if (jp.isExpectedStartArrayToken()) {
      List<MassageData> arrayList = new ArrayList<>();
      String key = jp.nextTextValue();
      do {
        if (StringUtil.isNotBlank(key)) {
          arrayList.add(MassageData.builder().value(key).build());
        }
        key = jp.nextTextValue();
      } while (StringUtil.isNotBlank(key));
      return arrayList;
    }
    return ListUtils.EMPTY_LIST;
  }
}
