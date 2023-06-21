package cn.asany.flowable.core.filters;

/**
 * 任务状态
 *
 * @author limaofeng
 */
public enum TaskStatus {

  /** 创建（Created）：任务已创建但尚未开始处理。 */
  CREATED("Created"),
  /** 待处理（Pending）：任务已分配给某个处理者，但尚未开始处理。 */
  PENDING("Pending"),
  /** 进行中（In Progress）：任务正在进行中，处理者正在执行相关操作。 */
  IN_PROGRESS("In Progress"),
  /** 挂起（Suspended）：任务被暂时挂起，暂停处理流程。 */
  SUSPENDED("Suspended"),
  /** 完成（Completed）：任务已完成，相关操作已经执行完毕。 */
  COMPLETED("Completed"),
  /** 取消（Canceled）：任务被取消，相关操作不再需要执行 */
  CANCELED("Canceled");

  private final String value;

  TaskStatus(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
