package cn.asany.shanhai.core.runners;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.utils.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;

/**
 * 初始化 SCALAR
 *
 * @author limaofeng
 */
// @Component
@Profile("!test")
public class PresetModelCommandLineRunner implements CommandLineRunner {

  @Autowired private ModelService modelService;

  @Override
  public void run(String... args) {
    // 基础类型
    modelService.saveInBatch(ModelUtils.DEFAULT_TYPES.toArray(new Model[0]));
  }
}
