package cn.asany.shanhai.core.support.graphql;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.ModelDelegate;
import cn.asany.shanhai.core.bean.ModelEndpoint;
import cn.asany.shanhai.core.bean.enums.ModelDelegateType;
import cn.asany.shanhai.core.support.dao.ModelRepository;
import cn.asany.shanhai.core.support.graphql.resolvers.GraphQLDelegateResolver;
import lombok.SneakyThrows;
import org.jfantasy.framework.util.common.ClassUtil;

import java.lang.reflect.Constructor;

public class ModelDelegateFactory {

    @SneakyThrows
    public DelegateHandler build(Model model, ModelEndpoint endpoint, ModelRepository repository, ModelDelegate delegate) {
        if (delegate.getType() == ModelDelegateType.Mock) {
            Class clazz = ClassUtil.forName(delegate.getDelegateClassName());
            Constructor<GraphQLDelegateResolver> constroctor = clazz.getConstructor(Model.class, ModelEndpoint.class, ModelRepository.class);
            GraphQLDelegateResolver _delegate = constroctor.newInstance(model, endpoint, repository);
            return new DefaultDelegateHandler(_delegate);
        }
        return null;
    }

}
