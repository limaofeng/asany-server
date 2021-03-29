package cn.asany.shanhai.core.runners;

import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.core.support.model.FieldType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

/**
 * 初始化 SCALAR
 *
 * @author limaofeng
 */
@Component
@ConditionalOnExpression("#{!environment.getProperty(\"spring.profiles.active\").contains(\"test\")}")
public class PresetModelCommandLineRunner implements CommandLineRunner {

    @Autowired
    private ModelService modelService;

    @Override
    public void run(String... args) {
        // 基础类型
        modelService.saveInBatch(
            FieldType.ID,
            FieldType.Int,
            FieldType.Float,
            FieldType.String,
            FieldType.Boolean,
            FieldType.Date
        );
        // 默认类型 PageInfo
//        modelService.saveInBatch(
//            PageInfo
//        );


    }
}
