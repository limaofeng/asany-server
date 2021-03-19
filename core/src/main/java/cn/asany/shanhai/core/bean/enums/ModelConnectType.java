package cn.asany.shanhai.core.bean.enums;

public enum ModelConnectType {
    INPUT(ModelRelationType.SUBJECTION, "INPUT");

    public ModelRelationType type;
    public String relation;

    ModelConnectType(ModelRelationType type, String relation) {
        this.type = type;
        this.relation = relation;
    }
}
