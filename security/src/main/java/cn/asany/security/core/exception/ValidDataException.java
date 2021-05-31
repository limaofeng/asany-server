package cn.asany.security.core.exception;

import org.jfantasy.framework.error.ValidationException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: guoyong
 * @description: 角色/权限部分异常处理
 * @create: 2020/6/9 23:48
 */
public class ValidDataException extends RuntimeException {

    protected static Map<String, String> errMap = new HashMap<>();
    public static final String ROLE_NOTEXISTS = "err.role.notexists";
    public static final String ROLETYPE_EXISTS = "err.roletype.exists";
    public static final String ROLETYPE_NOTEXISTS = "err.roletype.notexists";
    public static final String ROLETYPE_HAS_ROLES = "err.roletype.hasroles";
    public static final String PERMITYPE_EXISTS = "err.permitype.exists";
    public static final String PERMITYPE_NOTEXISTS = "err.permitype.notexists";
    public static final String PERMITYPE_HAS_PERMISSIONS = "err.permitype.haspermissions";
    public static final String PERMISSION_EXISTS = "err.permission.exists";
    public static final String PERMISSION_NOTEXISTS = "err.permission.notexists";
    public static final String PERMISSION_HAS_PERMISSIONS = "err.permission.haspermissions";
    public static final String PARAM_INPUT_ERROR = "err.input.errvalue";

    static {
        errMap.put(ROLE_NOTEXISTS, "该角色ID[ {0} ]不存在");
        errMap.put(ROLETYPE_EXISTS, "该角色分类ID[ {0} ]已经存在");
        errMap.put(ROLETYPE_NOTEXISTS, "该角色分类ID[ {0} ]不存在");
        errMap.put(ROLETYPE_HAS_ROLES, "该角色分类[ {0} ]下配置有角色，需要先删除角色定义");
        errMap.put(PERMITYPE_EXISTS, "该权限分类ID[ {0} ]已经存在");
        errMap.put(PERMITYPE_NOTEXISTS, "该权限分类ID[ {0} ]不存在");
        errMap.put(PERMITYPE_HAS_PERMISSIONS, "该权限分类[ {0} ]下配置有权限，需要先删除权限定义");
        errMap.put(PERMISSION_EXISTS, "该权限ID[ {0} ]已经存在");
        errMap.put(PERMISSION_NOTEXISTS, "该权限ID[ {0} ]不存在");
        errMap.put(PERMISSION_HAS_PERMISSIONS, "该权限[ {0} ]下配置有权限，需要先删除拥有者权限");
        errMap.put(PARAM_INPUT_ERROR, "参数输入错误");
    }

    public ValidDataException(String prop, String ...values) {
        String value = errMap.get(prop);
        if(value == null) {
            throw new ValidationException(prop);
        } else {
            int index = 0;
            for(String v : values) {
                value = value.replaceAll("\\{"+index+++"}", v);
            }
            throw new ValidationException(value);
        }
    }
}
