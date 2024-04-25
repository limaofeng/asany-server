/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.security.core.domain;

import static org.junit.jupiter.api.Assertions.*;

import cn.asany.security.core.domain.converter.ConditionConverter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.jackson.JSON;
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
