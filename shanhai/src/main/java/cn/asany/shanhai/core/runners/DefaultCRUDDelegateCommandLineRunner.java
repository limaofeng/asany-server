package cn.asany.shanhai.core.runners;

import cn.asany.shanhai.core.bean.ModelDelegate;
import cn.asany.shanhai.core.bean.enums.ModelDelegateType;
import cn.asany.shanhai.core.service.ModelDelegateService;
import cn.asany.shanhai.core.support.graphql.resolvers.base.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;

// @Component
@Profile("!test")
public class DefaultCRUDDelegateCommandLineRunner implements CommandLineRunner {

  @Autowired private ModelDelegateService modelDelegateService;

  @Override
  public void run(String... args) throws Exception {
    ModelDelegate CREATE =
        ModelDelegate.builder()
            .name("创建对象")
            .type(ModelDelegateType.Base)
            .delegateClassName(BaseMutationCreateDataFetcher.class.getName())
            .build();
    ModelDelegate UPDATE =
        ModelDelegate.builder()
            .name("更新对象")
            .type(ModelDelegateType.Base)
            .delegateClassName(BaseMutationUpdateDataFetcher.class.getName())
            .build();
    ModelDelegate DELETE =
        ModelDelegate.builder()
            .name("删除对象")
            .type(ModelDelegateType.Base)
            .delegateClassName(BaseMutationDeleteDataFetcher.class.getName())
            .build();
    ModelDelegate GET =
        ModelDelegate.builder()
            .name("获取单个对象")
            .type(ModelDelegateType.Base)
            .delegateClassName(BaseQueryGetDataFetcher.class.getName())
            .build();
    ModelDelegate FIND_ALL =
        ModelDelegate.builder()
            .name("查询全部")
            .type(ModelDelegateType.Base)
            .delegateClassName(BaseQueryFindAllDataFetcher.class.getName())
            .build();
    modelDelegateService.saveInBatch(CREATE, UPDATE, DELETE, GET, FIND_ALL);
  }
}
