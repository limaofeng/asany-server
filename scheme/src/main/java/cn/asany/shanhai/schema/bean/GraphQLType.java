package cn.asany.shanhai.schema.bean;

public enum GraphQLType {
    /**
     * 标量
     */
    Scalar,
    /**
     * 输入对象
     */
    InputObject,
    /**
     * 对象
     */
    Object,
    /**
     * 枚举型
     */
    Enum,
    /**
     * 联合
     */
    Union,
    /**
     * 接口
     */
    Interface
}
