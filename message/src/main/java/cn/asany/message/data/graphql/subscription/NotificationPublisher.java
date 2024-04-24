package cn.asany.message.data.graphql.subscription;

import cn.asany.message.core.Notification;
import net.asany.jfantasy.graphql.publishers.BasePublisher;
import org.springframework.stereotype.Component;

/**
 * 通知发布者
 *
 * @author limaofeng
 */
@Component
public class NotificationPublisher extends BasePublisher<Notification> {}
