package cn.asany.security.core.bean.enums;

/**
 * @author limaofeng
 */

public enum UserType {
    /**
     * 个人用户
     */
    personal("个人"),
    /**
     * 员工
     */
    employee("员工"),
    /**
     * 管理员
     */
    admin("管理员");

    private String value;

    UserType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
