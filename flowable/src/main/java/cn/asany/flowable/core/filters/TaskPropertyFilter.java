package cn.asany.flowable.core.filters;

import org.flowable.task.api.TaskQuery;
import org.flowable.task.service.TaskService;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.spring.SpringBeanUtils;

/**
 * 任务查询过滤器
 *
 * @author limaofeng
 */
public class TaskPropertyFilter extends PropertyFilterBuilder<TaskPropertyFilter, TaskQuery> {

  protected TaskPropertyFilter() {
    super(SpringBeanUtils.getBean(TaskService.class).createTaskQuery());
  }

  public static TaskPropertyFilter newFilter() {
    return new TaskPropertyFilter();
  }

  @Override
  public TaskQuery build() {
    return this.context;
  }
}
