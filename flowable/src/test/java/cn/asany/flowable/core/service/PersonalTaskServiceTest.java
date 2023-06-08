package cn.asany.flowable.core.service;

import cn.asany.flowable.TestApplication;
import cn.asany.flowable.core.domain.PersonalTask;
import java.util.ArrayList;

import cn.asany.flowable.core.filters.TaskPropertyFilter;
import lombok.extern.slf4j.Slf4j;
import org.flowable.task.api.TaskInfo;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PersonalTaskServiceTest {

  @Autowired private PersonalTaskService personalTaskService;

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @Test
  void getTasks() {
    Page<TaskInfo> page =
        personalTaskService.findPage(Pageable.ofSize(5), TaskPropertyFilter.newFilter().equal("owner", "1"));
    for (TaskInfo task : page.getContent()) {
      log.info("getTasks: {}", task);
    }
  }

  @Test
  void createTask() {
    personalTaskService.createTask(
        PersonalTask.builder().uid(1L).name("测试标题").description("测试内容").build());
  }

  @Test
  void completeTask() {
    String taskId = "26efa3f2-04f2-11ee-8edb-42636b40de1a";
    personalTaskService.completeTask(taskId);

    TaskInfo task = personalTaskService.getTask(taskId);

    log.info("task: {}", task);
  }

  @Test
  void getTask() {
    String taskId = "26efa3f2-04f2-11ee-8edb-42636b40de1a";
    TaskInfo task = personalTaskService.getTask(taskId);

    log.info("task: {}", task);
  }
}
