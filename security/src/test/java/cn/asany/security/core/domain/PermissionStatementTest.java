package cn.asany.security.core.domain;

import static org.junit.jupiter.api.Assertions.*;

import cn.asany.security.core.domain.converter.ConditionConverter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.jackson.JSON;
import org.junit.jupiter.api.Test;

@Slf4j
class PermissionStatementTest {

  @Test
  void testCondition() {
    Map<String, Map<String, List<String>>> condition = new HashMap<>();

    Map<String, List<String>> statusEquals = new HashMap<>();
    List<String> statuses = new ArrayList<>();
    statuses.add("PUBLISHED");
    statuses.add("SCHEDULED");
    statusEquals.put("cms:article::status", statuses);

    condition.put("Equals", statusEquals);

    String jsonStr = JSON.serialize(condition);

    log.debug("json: " + jsonStr);

    ConditionConverter converter = new ConditionConverter();

    List<PermissionCondition> conditions = converter.convertToEntityAttribute(jsonStr);

    assertFalse(conditions.isEmpty());

    String dbData = converter.convertToDatabaseColumn(conditions);

    assertEquals(jsonStr, dbData);
  }
}
