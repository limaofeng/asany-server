package cn.asany.security.oauth.bean.enums;

public enum TokenType {
    BEARER("bearer"),
    MAC("mac");

    private String value;

    TokenType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
