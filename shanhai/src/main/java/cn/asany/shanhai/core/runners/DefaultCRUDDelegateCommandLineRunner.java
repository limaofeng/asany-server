package cn.asany.shanhai.core.runners;

import cn.asany.shanhai.core.domain.ModelDelegate;
import cn.asany.shanhai.core.domain.enums.ModelDelegateType;
import cn.asany.shanhai.core.service.ModelDelegateService;
import cn.asany.shanhai.core.support.graphql.resolvers.base.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class DefaultCRUDDelegateCommandLineRunner implements CommandLineRunner {

  @Autowired private ModelDelegateService modelDelegateService;

  @Override
  public void run(String... args) throws Exception {
    ModelDelegate CREATE =
        ModelDelegate.builder()
            .id(1L)
            .name("创建对象")
            .type(ModelDelegateType.Base)
            .delegateClassName(BaseMutationCreateDataFetcher.class.getName())
            .build();
    ModelDelegate UPDATE =
        ModelDelegate.builder()
            .id(2L)
            .name("更新对象")
            .type(ModelDelegateType.Base)
            .delegateClassName(BaseMutationUpdateDataFetcher.class.getName())
            .build();
    ModelDelegate DELETE =
        ModelDelegate.builder()
            .id(3L)
            .name("删除对象")
            .type(ModelDelegateType.Base)
            .delegateClassName(BaseMutationDeleteDataFetcher.class.getName())
            .build();
    ModelDelegate GET =
        ModelDelegate.builder()
            .id(4L)
            .name("获取单个对象")
            .type(ModelDelegateType.Base)
            .delegateClassName(BaseQueryGetDataFetcher.class.getName())
            .build();
    ModelDelegate FIND_ALL =
        ModelDelegate.builder()
            .id(5L)
            .name("查询全部")
            .type(ModelDelegateType.Base)
            .delegateClassName(BaseQueryFindAllDataFetcher.class.getName())
            .build();
    ModelDelegate CONNECTION =
        ModelDelegate.builder()
            .id(6L)
            .name("分页查询")
            .type(ModelDelegateType.Base)
            .delegateClassName(BaseQueryConnectionDataFetcher.class.getName())
            .build();
    modelDelegateService.saveInBatch(CREATE, UPDATE, DELETE, GET, FIND_ALL, CONNECTION);
  }
}
