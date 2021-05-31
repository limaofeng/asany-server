package cn.asany.security.core.bean.enums;

public enum ResourceType {

    /**
     * url
     */
    url("url");

    private String value;

    ResourceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}