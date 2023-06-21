package cn.asany.flowable.core.domain;

import cn.asany.flowable.engine.form.model.CustomFormModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import org.flowable.form.model.FormField;
import org.flowable.form.model.FormOutcome;
import org.flowable.ui.modeler.domain.Model;
import org.jfantasy.framework.jackson.JSON;

/**
 * 表单模型
 *
 * @author limaofeng
 */
@JsonIgnoreProperties({"modelEditorJson"})
public class FormModel extends Model {

  @JsonIgnore private CustomFormModel internalFormModel;

  private CustomFormModel parseModelEditor() {
    if (this.internalFormModel != null) {
      return this.internalFormModel;
    }
    if (this.modelEditorJson == null) {
      return this.internalFormModel = new CustomFormModel();
    }
    return this.internalFormModel = JSON.deserialize(this.modelEditorJson, CustomFormModel.class);
  }

  public String getComponent() {
    return this.parseModelEditor().getComponent();
  }

  public void setComponent(String component) {
    this.parseModelEditor().setComponent(component);
  }

  public List<FormOutcome> getOutcomes() {
    return this.parseModelEditor().getOutcomes();
  }

  public void setOutcomes(List<FormOutcome> outcomes) {
    this.parseModelEditor().setOutcomes(outcomes);
  }

  public String getOutcomeVariableName() {
    return this.parseModelEditor().getOutcomeVariableName();
  }

  public void setOutcomeVariableName(String outcomeVariableName) {
    this.parseModelEditor().setOutcomeVariableName(outcomeVariableName);
  }

  public List<FormField> getFields() {
    return this.parseModelEditor().getFields();
  }

  public void setFields(List<FormField> fields) {
    this.parseModelEditor().setFields(fields);
  }
}
