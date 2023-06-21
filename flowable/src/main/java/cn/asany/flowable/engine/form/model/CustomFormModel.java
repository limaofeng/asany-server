package cn.asany.flowable.engine.form.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.flowable.form.api.FormModel;
import org.flowable.form.model.SimpleFormModel;

/**
 * 自定义表单模型
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomFormModel extends SimpleFormModel implements FormModel {

  /** 表单对应的自定义建模ID */
  private String modelId;
  /** 表单组件 */
  private String component;
}
