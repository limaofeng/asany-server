package cn.asany.crm.support.domain.enums;

import lombok.Getter;

@Getter
public enum TicketPriority {
  LOW("低"),
  NORMAL("普通"),
  HIGH("高"),
  URGENT("紧急");

  private final String name;

  TicketPriority(String name) {
    this.name = name;
  }
}
