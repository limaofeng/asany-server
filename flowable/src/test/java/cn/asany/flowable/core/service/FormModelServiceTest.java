package cn.asany.flowable.core.service;

import cn.asany.flowable.TestApplication;
import cn.asany.flowable.core.domain.FormModel;
import cn.asany.flowable.engine.form.model.CustomFormModel;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.flowable.form.model.FormField;
import org.flowable.form.model.SimpleFormModel;
import org.flowable.ui.common.model.RemoteUser;
import org.flowable.ui.common.security.FlowableAppUser;
import org.jfantasy.framework.dao.Page;
import org.jfantasy.framework.dao.Pagination;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.util.common.PathUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockPart;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FormModelServiceTest {

  @Autowired private FormModelService formModelService;

  @Test
  void importFormModel() throws IOException {
    HttpServletRequest request = new MockHttpServletRequest();
    URL url = PathUtil.getContextClassLoaderResource("custom-form.bpmn20.xml");
    Part file =
        new MockPart(
            "form_model", "custom-form.bpmn20.xml", Files.readAllBytes(Paths.get(url.getPath())));
    formModelService.importFormModel(LoginUser.builder().uid(1L).build(), request, file);
  }

  @Test
  void save() {
    CustomFormModel formModel = new CustomFormModel();
    formModel.setKey("xxx0001-key");
    formModel.setName("formModel---1");
    SimpleFormModel result = formModelService.save(formModel, 1L);
    log.info("result: {}", JSON.serialize(result));
  }

  @Test
  void get() {
    FormModel formModel = formModelService.get("ccf2c9fd-09c3-11ee-9029-1a5f929a323e");
    log.info("result: {}", JSON.serialize(formModel));
  }

  @Test
  void deploy() {
    formModelService.deploy("ccf2c9fd-09c3-11ee-9029-1a5f929a323e");
  }

  @Test
  void update() {
    RemoteUser user = new RemoteUser();
    user.setId("1");
    Authentication authentication =
        new UsernamePasswordAuthenticationToken(
            new FlowableAppUser(user, "1L", new ArrayList<>()), "");
    SecurityContextHolder.getContext().setAuthentication(authentication);

    FormModel formModel = new FormModel();
    formModel.setKey("xxx0001-key");
    formModel.setName("formModel123");
    formModel.setFields(new ArrayList<>());

    FormField formField = new FormField();
    formField.setId("1");
    formField.setName("name");
    formField.setType("string");

    Map<String, Object> params = new HashMap<>();
    params.put("renderer", "DepartmentSelector");
    formField.setParams(params);
    formField.setRequired(true);
    formField.setPlaceholder("请输入姓名");
    formField.setReadOnly(false);

    formModel.getFields().add(formField);

    formModelService.update("ccf2c9fd-09c3-11ee-9029-1a5f929a323e", formModel);
  }

  @Test
  void findPage() {
    Page<FormModel> page =
        formModelService.findPage(
            Pagination.newPager(10), PropertyFilter.newFilter().equal("user", 1L));
    log.info("page size {}", page.getTotalCount());
  }
}
