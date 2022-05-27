package cn.asany.shanhai.core.domain.enums;

public enum ModelConnectType {
  INPUT(ModelRelationType.SUBJECTION, "INPUT"),
  TYPE(ModelRelationType.SUBJECTION, "TYPE");

  public ModelRelationType type;
  public String relation;

  ModelConnectType(ModelRelationType type, String relation) {
    this.type = type;
    this.relation = relation;
  }
}
