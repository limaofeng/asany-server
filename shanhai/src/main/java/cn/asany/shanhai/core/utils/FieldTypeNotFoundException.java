package cn.asany.shanhai.core.utils;

public class FieldTypeNotFoundException extends RuntimeException {
    public FieldTypeNotFoundException(String code) {
        super("Code = " + code + " 不存在");
    }
}
