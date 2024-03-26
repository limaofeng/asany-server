package cn.asany.crm.support.domain.enums;

import lombok.Getter;

@Getter
public enum TicketStatus {
  NEW("新建"),
  IN_PROGRESS("进行中"),
  SUSPENDED("暂停"),
  RESOLVED("已解决"),
  REOPEN("重新开启");

  private final String name;

  TicketStatus(String name) {
    this.name = name;
  }
}
