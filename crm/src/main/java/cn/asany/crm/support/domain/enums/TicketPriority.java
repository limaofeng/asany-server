package cn.asany.crm.support.domain.enums;

public enum TicketPriority {
  LOW("低"),
  NORMAL("普通"),
  HIGH("高"),
  URGENT("紧急");

  private String name;

  TicketPriority(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
