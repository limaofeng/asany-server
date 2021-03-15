package cn.asany.shanhai.core.support;

import cn.asany.shanhai.core.bean.ModelFieldMetadata;

public interface FieldType {
    String ID = "ID";
    String STRING = "String";
    String Number = "Number";
    String Date = "Date";

    String getId();

    /**
     * 类型名称
     *
     * @return
     */
    String getName();

    /**
     * 对应的 Java Class
     *
     * @return
     */
    String getJavaType(ModelFieldMetadata metadata);

    /**
     * 对应的 GraphQL 类型
     *
     * @return
     */
    String getGraphQLType();

    /**
     * 获取字段设置信息
     *
     * @return
     */
    DatabaseColumn getColumn(ModelFieldMetadata metadata);
}
