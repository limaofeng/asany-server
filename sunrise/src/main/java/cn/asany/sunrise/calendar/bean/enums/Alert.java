package cn.asany.sunrise.calendar.bean.enums;

/**
 * 提醒类型
 *
 * @author limaofeng
 */
public enum Alert {
  /** 无 */
  NONE,
  /** 与时间时间一致 */
  AT_TIME_OF_EVEN,
  /** 前X分钟 */
  MINUTES_BEFORE,
  /** 前X小时 */
  HOURS_BEFORE,
  /** 前X天 */
  DAYS_BEFORE,
  /** 后X分钟 */
  MINUTES_AFTER,
  /** 后X小时 */
  HOURS_AFTER,
  /** 后X天 */
  DAYS_AFTER,
  /** 指定时间 */
  ON_DATA;
}
