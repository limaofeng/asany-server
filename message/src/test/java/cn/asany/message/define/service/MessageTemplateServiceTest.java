package cn.asany.message.define.service;

import static org.junit.jupiter.api.Assertions.*;

import cn.asany.message.TestApplication;
import cn.asany.message.define.domain.MessageTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cn.asany.message.define.domain.enums.TemplateType;
import cn.asany.message.define.domain.toys.MessageContent;
import cn.asany.message.define.domain.toys.VariableDefinition;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MessageTemplateServiceTest {

  @Autowired MessageTemplateService messageTemplateService;

  @Test
  void save() {
    MessageTemplate template = new MessageTemplate();
    template.setType(TemplateType.EMAIL);
    template.setName("新工单邮件通知");

    MessageContent content = getMessageContent();

    template.setContent(content);

    List<VariableDefinition> variableDefinitions = new ArrayList<>();
    variableDefinitions.add(VariableDefinition.builder()
        .name("no")
        .defaultValue("工单编号")
        .type("String")
        .required(true)
      .build());
    variableDefinitions.add(VariableDefinition.builder()
      .name("storeName")
      .description("门店名称")
      .type("String")
      .required(true)
      .build());
    
    template.setVariables(variableDefinitions);

    messageTemplateService.save(template);

  }

  @Test
  void updateEmailTemplate() {
    MessageTemplate template = messageTemplateService.findById(52L).orElseThrow();
    MessageContent content = getMessageContent();
    template.setContent(content);
    messageTemplateService.save(template);
  }

  private static MessageContent getMessageContent() {
    MessageContent content = new MessageContent();
    content.setTitle("新工单通知 - {{no}} - {{storeName}}");
    content.setContent("""
      您好！
            
      我们收到了来自 [客户名] 的新服务请求工单。请查看以下工单详情，并按照规定的服务级别协议进行处理。
            
      - **工单编号**：[工单编号]
      - **提交时间**：[提交时间]
      - **客户名称**：[客户名]
      - **服务类型**：[服务类型]
      - **优先级**：[优先级]
      - **问题概述**：[简短问题描述]
      - **详细描述**：
        [详细问题描述]
            
      请尽快登录到我们的工单管理系统查看详细信息，并根据客户的请求采取相应措施。
            
      [工单系统链接]
            
      感谢您的快速响应和负责任的服务态度！
            
      祝好，
      [您的公司名] 客户服务团队
      [联系电话]
      [公司网站]
            
      ---
      请不要直接回复此邮件，此邮箱不接收回复。如需联系，请使用工单系统或联系客户服务部。
      """);
    return content;
  }

  @Test
  void findById() {
    Optional<MessageTemplate> templateOptional = messageTemplateService.findById(1L);
    log.info("templateOptional:{}", templateOptional);
    assertTrue(templateOptional.isPresent());
  }
}
