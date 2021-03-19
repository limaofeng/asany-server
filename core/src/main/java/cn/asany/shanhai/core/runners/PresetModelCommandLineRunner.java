package cn.asany.shanhai.core.runners;

import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.support.model.FieldType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化 SCALAR
 *
 * @author limaofeng
 */
@Component
public class PresetModelCommandLineRunner implements CommandLineRunner {

    @Autowired
    private ModelService modelService;

    @Override
    public void run(String... args) {
        modelService.saveInBatch(
            FieldType.ID,
            FieldType.Int,
            FieldType.Float,
            FieldType.String,
            FieldType.Boolean,
            FieldType.Date
        );
    }
}
