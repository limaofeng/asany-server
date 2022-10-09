package cn.asany.shanhai.autoconfigure;

import cn.asany.shanhai.core.support.model.FieldType;
import cn.asany.shanhai.core.support.model.types.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义字段类型
 *
 * @author limaofeng
 */
@Configuration
public class CustomFieldTypeConfig {

  @Bean
  public FieldType<?, ?> singleLineTextOfStringField() {
    StringField field =
        new StringField(FieldTypeFamily.STRING, "single_line_text", "单行文本", "标题与段落标题");
    field.setIndex(0);
    return field;
  }

  @Bean
  public FieldType<?, ?> multiLineTextOfStringField() {
    StringField field = new StringField(FieldTypeFamily.STRING, "multi_line_text", "多行文本", "描述");
    field.setIndex(1);
    return field;
  }

  @Bean
  public FieldType<?, ?> markdownOfStringField() {
    StringField field =
        new StringField(FieldTypeFamily.STRING, "markdown", "Markdown", "Markdown编辑器");
    field.setIndex(2);
    return field;
  }

  @Bean
  public FieldType<?, ?> slugOfStringField() {
    StringField field = new StringField(FieldTypeFamily.STRING, "slug", "编码", "URL 友好标识符");
    field.setIndex(3);
    return field;
  }

  @Bean
  public FieldType<?, ?> richTextOfStringField() {
    return new StringField(FieldTypeFamily.TEXT, "rich_text", "富文本", "带格式的文本编辑器");
  }

  @Bean
  public FieldType<?, ?> numberOfIntField() {
    return new IntField(FieldTypeFamily.INTEGER, "number", "数字", "ID、数量等");
  }

  @Bean
  public FieldType<?, ?> floatOfIntField() {
    return new FloatField(FieldTypeFamily.FLOAT, "float", "浮点数", "评分、价格等");
  }

  @Bean
  public FieldType<?, ?> boolOfBooleanField() {
    return new BooleanField(FieldTypeFamily.BOOLEAN, "bool", "布尔", "是或否");
  }

  @Bean
  public FieldType<?, ?> dateOfDateField() {
    return new DateField(FieldTypeFamily.DATE, "date", "日期", "日历日期选择器");
  }

  @Bean
  public FieldType<?, ?> dateTimeOfDateField() {
    return new DateField(FieldTypeFamily.DATE_TIME, "date_time", "日期和时间", "带时间的日历日期选择器");
  }

  @Bean
  public FieldType<?, ?> jsonOfJSONField() {
    return new JSONField(FieldTypeFamily.JSON, "json", "JSON", "任意 JSON 结构");
  }

  @Bean
  public FieldType<?, ?> assetOfAssetField() {
    return new AssetField(FieldTypeFamily.ASSET, "asset", "静态资源", "支持任何文件类型");
  }

  @Bean
  public FieldType<?, ?> referenceField() {
    return new ReferenceField(FieldTypeFamily.RELATION, "reference", "关联", "内容的关系");
  }
}
