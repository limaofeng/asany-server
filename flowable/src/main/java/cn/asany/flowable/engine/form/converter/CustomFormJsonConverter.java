package cn.asany.flowable.engine.form.converter;

import cn.asany.flowable.engine.form.model.CustomFormModel;
import org.flowable.editor.form.converter.FlowableFormJsonException;
import org.flowable.editor.form.converter.FormJsonConverter;
import org.flowable.form.model.SimpleFormModel;
import org.jfantasy.framework.jackson.JSON;

/**
 * 自定义表单模型转换器
 *
 * @author limaofeng
 */
public class CustomFormJsonConverter extends FormJsonConverter {

  @Override
  public CustomFormModel convertToFormModel(String modelJson) {
    try {
      return JSON.getObjectMapper().readValue(modelJson, CustomFormModel.class);
    } catch (Exception e) {
      throw new FlowableFormJsonException("Error reading form json", e);
    }
  }

  @Override
  public String convertToJson(SimpleFormModel definition) {
    try {
      return JSON.getObjectMapper().writeValueAsString(definition);
    } catch (Exception e) {
      throw new FlowableFormJsonException("Error writing form json", e);
    }
  }
}
