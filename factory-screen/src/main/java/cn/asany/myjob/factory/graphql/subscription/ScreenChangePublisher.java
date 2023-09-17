package cn.asany.myjob.factory.graphql.subscription;

import cn.asany.myjob.factory.domain.Screen;
import org.jfantasy.graphql.publishers.BasePublisher;
import org.springframework.stereotype.Component;

/**
 * 通知发布者
 *
 * @author limaofeng
 */
@Component
public class ScreenChangePublisher extends BasePublisher<Screen> {}
