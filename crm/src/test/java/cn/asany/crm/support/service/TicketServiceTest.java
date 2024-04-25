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
package cn.asany.crm.support.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.spring.SpELUtil;
import org.junit.jupiter.api.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

@Slf4j
class TicketServiceTest {

  @Test
  void generateNo() {
    SpelExpressionParser parser = new SpelExpressionParser();
    // 'DR-' + #DateUtil.format('yyMMddHH') + '-' +
    // #StringUtil.addZeroLeft(#SequenceInfo.nextValue('Ticket|DR-' + #DateUtil.format('yyMMddHH')),
    // 4)
    Expression expression =
        parser.parseExpression(
            "'DR-' + #DateUtil.format('yyMMddHH') + '-' + #StringUtil.addZeroLeft(123, 4)");
    Map<String, Object> object = new HashMap<>();
    String no = expression.getValue(SpELUtil.createEvaluationContext(object), String.class);
    log.info("no: {}", no);
  }
}
