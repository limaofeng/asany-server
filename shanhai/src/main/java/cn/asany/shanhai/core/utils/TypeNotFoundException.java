package cn.asany.shanhai.core.utils;

public class TypeNotFoundException extends RuntimeException {
  public TypeNotFoundException(String code) {
    super("Code = " + code + " 不存在");
  }
}
