package cn.asany.shanhai.core.runners;

import cn.asany.shanhai.core.support.ModelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InitModelDaoCommandLineRunner implements CommandLineRunner {

    @Autowired
    private ModelFactory modelFactory;

    @Override
    @Transactional(readOnly = true)
    public void run(String... args) {
        modelFactory.loadDefaultModel();
    }
}
