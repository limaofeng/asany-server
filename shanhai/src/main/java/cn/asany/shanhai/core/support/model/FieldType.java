package cn.asany.shanhai.core.support.model;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.ModelFieldMetadata;
import cn.asany.shanhai.core.domain.enums.ModelType;
import cn.asany.shanhai.core.support.model.types.FieldTypeFamily;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.jfantasy.framework.dao.jpa.PropertyFilter.MatchType;

/**
 * 字段类型
 *
 * @author limaofeng
 */
@Converter
public interface FieldType<JAVA, DB> extends AttributeConverter<JAVA, DB> {
  /** 查询 */
  Model Query = Model.builder().type(ModelType.OBJECT).id(1L).code("Query").name("Query").build();
  /** 突变 */
  Model Mutation =
      Model.builder().type(ModelType.OBJECT).id(2L).code("Mutation").name("Mutation").build();
  /** ID */
  Model ID = Model.builder().type(ModelType.SCALAR).id(3L).code("ID").name("ID").build();
  /** 整数型 */
  Model Int = Model.builder().type(ModelType.SCALAR).id(4L).code("Int").name("整数型").build();
  /** 浮点型 */
  Model Float = Model.builder().type(ModelType.SCALAR).id(5L).code("Float").name("浮点型").build();
  /** 字符串 */
  Model String = Model.builder().type(ModelType.SCALAR).id(6L).code("String").name("字符串").build();
  /** 布尔型 */
  Model Boolean = Model.builder().type(ModelType.SCALAR).id(7L).code("Boolean").name("布尔型").build();
  /** 日期 */
  Model Date = Model.builder().type(ModelType.SCALAR).id(8L).code("Date").name("日期").build();

  String getId();

  default int getIndex() {
    return 0;
  }

  /**
   * 类型名称
   *
   * @return String
   */
  String getName();

  String getDescription();

  FieldTypeFamily getFamily();

  /**
   * 对应的 Java Class
   *
   * @return String
   */
  String getJavaType(ModelFieldMetadata metadata);

  /**
   * 对应的 GraphQL 类型
   *
   * @return String
   */
  String getGraphQLType();

  default String getHibernateType(ModelFieldMetadata metadata) {
    return getJavaType(metadata);
  }

  /**
   * 获取字段设置信息
   *
   * @return DatabaseColumn
   */
  default DatabaseColumn getColumn(ModelFieldMetadata metadata) {
    return DatabaseColumn.builder()
        .name(metadata.getDatabaseColumnName())
        .updatable(true)
        .nullable(true)
        .build();
  }

  @Override
  default DB convertToDatabaseColumn(JAVA attribute) {
    return (DB) attribute;
  }

  @Override
  default JAVA convertToEntityAttribute(DB dbData) {
    return (JAVA) dbData;
  }

  default MatchType[] filters() {
    return new MatchType[0];
  }
}
