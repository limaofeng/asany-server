package cn.asany.flowable.core.filters;

import org.flowable.task.api.TaskQuery;
import org.flowable.task.service.TaskService;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.spring.SpringBeanUtils;

public class TaskPropertyFilter extends PropertyFilterBuilder<TaskQuery> implements PropertyFilter {
  protected TaskPropertyFilter() {
    super(SpringBeanUtils.getBean(TaskService.class).createTaskQuery());
  }

  @Override
  public TaskQuery build() {
    return this.context;
  }
}
