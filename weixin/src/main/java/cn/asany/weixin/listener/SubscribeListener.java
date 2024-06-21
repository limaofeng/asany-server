/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.weixin.listener;

import cn.asany.weixin.framework.event.SubscribeEventListener;
import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.factory.WeixinSessionUtils;
import cn.asany.weixin.framework.message.EventMessage;
import cn.asany.weixin.framework.message.content.Event;
import cn.asany.weixin.framework.session.WeixinSession;
import cn.asany.weixin.service.FansService;
import java.util.concurrent.Executor;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.spring.SpringBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class SubscribeListener implements SubscribeEventListener {

  @Autowired private FansService fansService;

  @Override
  public void onSubscribe(final WeixinSession session, Event event, final EventMessage<?> message) {
    Executor executor = SpringBeanUtils.getBeanByType(Executor.class);
    assert executor != null;
    executor.execute(
        () -> {
          try {
            WeixinSessionUtils.saveSession(session);
            fansService.checkCreateMember(
                session.getWeixinApp().getId(), message.getFromUserName());
          } catch (WeixinException e) {
            log.error(e.getMessage(), e);
          } finally {
            WeixinSessionUtils.closeSession();
          }
        });
  }
}
