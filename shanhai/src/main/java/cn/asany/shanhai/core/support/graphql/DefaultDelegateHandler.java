package cn.asany.shanhai.core.support.graphql;

import cn.asany.shanhai.core.support.graphql.resolvers.DelegateDataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.reflect.MethodProxy;

public class DefaultDelegateHandler implements DelegateHandler {

  private DelegateDataFetcher delegate;
  private MethodProxy method;

  public DefaultDelegateHandler(DelegateDataFetcher delegate) {
    this.delegate = delegate;
    this.method = ClassUtil.getMethodProxy(this.delegate.getClass(), this.delegate.method());
  }

  @Override
  public Object invoke(DataFetchingEnvironment environment) {
    Object[] args = this.delegate.args(environment);
    return this.method.invoke(this.delegate, args);
  }
}
