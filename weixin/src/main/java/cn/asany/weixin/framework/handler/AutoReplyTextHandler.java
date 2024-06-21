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
package cn.asany.weixin.framework.handler;

import cn.asany.weixin.framework.message.EmptyMessage;
import cn.asany.weixin.framework.message.TextMessage;
import cn.asany.weixin.framework.message.WeixinMessage;
import cn.asany.weixin.framework.session.WeixinSession;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/** 自动回复处理器 */
@Slf4j
public class AutoReplyTextHandler extends TextWeixinHandler {

  private List<AutoReplyHandler> handlers = new ArrayList<>();

  @Override
  protected WeixinMessage<?> handleTextMessage(WeixinSession session, TextMessage message) {
    String keyword = message.getContent();
    for (AutoReplyHandler handler : handlers) {
      if (handler.handle(keyword)) {
        log.debug(keyword + " => " + handler);
        return handler.autoReply(keyword);
      }
    }
    return new EmptyMessage();
  }

  public void setHandlers(List<AutoReplyHandler> handlers) {
    this.handlers = handlers;
  }
}
