package cn.asany.shanhai.core.utils;

import cn.asany.shanhai.core.domain.Model;
import com.github.jknack.handlebars.Template;
import lombok.SneakyThrows;
import org.jfantasy.framework.util.HandlebarsTemplateUtils;
import org.springframework.beans.factory.InitializingBean;

public class HibernateMappingHelper implements InitializingBean {

  private Template template;

  @Override
  public void afterPropertiesSet() throws Exception {
    template = HandlebarsTemplateUtils.template("/hibernate-mapping");
  }

  /**
   * 生成 Hibernate Mapping XML
   *
   * @param model 实体
   */
  @SneakyThrows
  public String generateXML(Model model) {
    return template.apply(new TemplateDataOfModel(model));
  }
}
