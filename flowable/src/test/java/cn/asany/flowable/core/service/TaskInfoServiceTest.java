package cn.asany.flowable.core.service;

import cn.asany.flowable.TestApplication;
import cn.asany.flowable.core.domain.PersonalTask;
import cn.asany.flowable.core.filters.TaskPropertyFilter;
import lombok.extern.slf4j.Slf4j;
import org.flowable.task.api.TaskInfo;
import org.flowable.ui.common.security.SecurityUtils;
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
class TaskInfoServiceTest {

  @Autowired private TaskInfoService taskInfoService;

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @Test
  void getTasks() {

    Page<TaskInfo> page =
        taskInfoService.findPage(
            Pageable.ofSize(5), TaskPropertyFilter.newFilter(true).equal("owner", "1"));
    log.info("my task: {}", page.getTotalElements());
    for (TaskInfo taskInfo : page.getContent()) {
      log.info("taskInfo assignee: {}", taskInfo.getAssignee());
    }

    page =
        taskInfoService.findPage(
            Pageable.ofSize(5),
            TaskPropertyFilter.newFilter(true).equal("owner", "1").equal("incomplete", true));
    log.info("incomplete task: {}", page.getTotalElements());

    page =
        taskInfoService.findPage(
            Pageable.ofSize(5),
            TaskPropertyFilter.newFilter(true).equal("owner", "1").equal("completed", true));
    log.info("completed task: {}", page.getTotalElements());
  }

  @Test
  void createPersonalTask() {
    TaskInfo taskInfo =
        taskInfoService.createPersonalTask(
            PersonalTask.builder().priority(12).name("读书10分钟").description("如题").build(),
            SecurityUtils.getCurrentUserObject());
    log.info("taskInfo: {}", taskInfo);
  }

  @Test
  void completeTask() {
    String taskId = "26efa3f2-04f2-11ee-8edb-42636b40de1a";
    taskInfoService.completeTask(taskId);

    TaskInfo task = taskInfoService.getTask(taskId);

    log.info("task: {}", task);
  }

  @Test
  void getTask() {
    String taskId = "26efa3f2-04f2-11ee-8edb-42636b40de1a";
    TaskInfo task = taskInfoService.getTask(taskId);
    log.info("task: {}", task);
  }
}
