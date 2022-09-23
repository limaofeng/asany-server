package cn.asany.shanhai.core.utils;

import org.jfantasy.framework.error.ValidationException;

public class DuplicateFieldException extends ValidationException {
  public DuplicateFieldException(String type, String name) {
    super("字段重复 type = " + type + ",name = " + name + " 不存在");
  }
}
