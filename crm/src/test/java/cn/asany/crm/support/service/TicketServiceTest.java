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
