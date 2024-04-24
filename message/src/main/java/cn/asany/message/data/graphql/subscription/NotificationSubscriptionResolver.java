package cn.asany.message.data.graphql.subscription;

import cn.asany.message.core.Notification;
import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import net.asany.jfantasy.framework.security.LoginUser;
import net.asany.jfantasy.framework.security.SpringSecurityUtils;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

/**
 * 通知订阅
 *
 * @author limaofeng
 */
@Component
public class NotificationSubscriptionResolver implements GraphQLSubscriptionResolver {

  private final NotificationPublisher notificationPublisher;

  public NotificationSubscriptionResolver(NotificationPublisher notificationPublisher) {
    this.notificationPublisher = notificationPublisher;
  }

  Publisher<Notification> notification(List<String> types, DataFetchingEnvironment env) {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    if (user == null) {
      return notificationPublisher.getPublisher(
          (notification) -> {
            if (types.isEmpty()) {
              return notification.isSystemMessage();
            }
            return types.contains(notification.getType().getId()) && notification.isSystemMessage();
          });
    }
    return notificationPublisher.getPublisher(
        notification -> {
          boolean isMyMessage =
              notification.isSystemMessage() || notification.getUserId().equals(user.getUid());
          if (types.isEmpty()) {
            return isMyMessage;
          }
          return types.contains(notification.getType().getId()) && isMyMessage;
        });
  }
}
