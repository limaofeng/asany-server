package cn.asany.shanhai.core.support.model;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelFieldMetadata;
import cn.asany.shanhai.core.bean.enums.ModelType;

public interface FieldType {
    /**
     * ID
     */
    Model ID = Model.builder().type(ModelType.SCALAR).id(1L).code("ID").name("ID").build();
    /**
     * 整数型
     */
    Model Int = Model.builder().type(ModelType.SCALAR).id(2L).code("Int").name("整数型").build();
    /**
     * 浮点型
     */
    Model Float = Model.builder().type(ModelType.SCALAR).id(3L).code("Float").name("浮点型").build();
    /**
     * 字符串
     */
    Model String = Model.builder().type(ModelType.SCALAR).id(4L).code("String").name("字符串").build();
    /**
     * 布尔型
     */
    Model Boolean = Model.builder().type(ModelType.SCALAR).id(5L).code("Boolean").name("布尔型").build();
    /**
     * 日期
     */
    Model Date = Model.builder().type(ModelType.SCALAR).id(6L).code("Date").name("日期").build();

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
    String getGraphQLType(ModelFieldMetadata metadata);

    /**
     * 获取字段设置信息
     *
     * @return
     */
    DatabaseColumn getColumn(ModelFieldMetadata metadata);
}
