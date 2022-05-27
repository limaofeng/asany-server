package cn.asany.shanhai.core.support.graphql;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.ModelDelegate;
import cn.asany.shanhai.core.domain.ModelEndpoint;
import cn.asany.shanhai.core.domain.enums.ModelDelegateType;
import cn.asany.shanhai.core.support.dao.ModelRepository;
import cn.asany.shanhai.core.support.graphql.resolvers.DelegateDataFetcher;
import java.lang.reflect.Constructor;
import lombok.SneakyThrows;
import org.jfantasy.framework.util.common.ClassUtil;

public class ModelDelegateFactory {

  @SneakyThrows
  public DelegateHandler build(
      Model model, ModelEndpoint endpoint, ModelRepository repository, ModelDelegate delegate) {
    if (delegate.getType() == ModelDelegateType.Base) {
      Class clazz = ClassUtil.forName(delegate.getDelegateClassName());
      Constructor<DelegateDataFetcher> constroctor =
          clazz.getConstructor(Model.class, ModelEndpoint.class, ModelRepository.class);
      DelegateDataFetcher _delegate = constroctor.newInstance(model, endpoint, repository);
      return new DefaultDelegateHandler(_delegate);
    }
    return null;
  }
}
