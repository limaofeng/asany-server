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
package cn.asany.message.api;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * 简单消息
 *
 * @author limaofeng
 */
@Data
@Builder
public class SimpleMessage implements Message, Serializable {
  /** 消息发送者 */
  private String from;

  /** 消息接收者 */
  private String[] to;

  /** 消息发送时间 */
  private Date sentDate;

  /** 消息主题 */
  private String subject;

  /** 消息内容 */
  private String text;

  /** 模板参数 */
  private Map<String, Object> templateParams;

  /** 附加信息 */
  @ToString.Exclude private Map<String, Object> extra;

  /** 消息跳转地址 */
  private String uri;

  private IChannelConfig config;

  public void set(String key, Object value) {
    this.extra.put(key, value);
  }

  public Object get(String key) {
    return this.extra.get(key);
  }

  public <T> T getOriginalMessage() {
    return (T) get("originalMessage");
  }

  public <T> T getOriginalMessageType() {
    return (T) get("originalMessageType");
  }

  public static class SimpleMessageBuilder {

    public SimpleMessageBuilder originalMessage(Object originalMessage) {
      if (this.extra == null) {
        this.extra = new HashMap<>();
      }
      this.extra.put("originalMessage", originalMessage);
      return this;
    }

    public SimpleMessageBuilder originalMessageType(Object originalMessageType) {
      if (this.extra == null) {
        this.extra = new HashMap<>();
      }
      this.extra.put("originalMessageType", originalMessageType);
      return this;
    }
  }
}
