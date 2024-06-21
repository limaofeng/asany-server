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

/**
 * 消息发送者解析器
 *
 * @author limaofeng
 */
public interface MessageChannelResolver {

  /**
   * 解析消息发送者
   *
   * @param id 消息发送者定义ID
   * @return 消息发送者
   * @throws MessageException 消息异常
   */
  MessageChannel<? extends Message> resolve(String id) throws MessageException;

  /**
   * 解析消息发送者
   *
   * @param id 消息发送者定义ID
   * @param config 消息发送者配置
   * @return 消息发送者
   * @throws MessageException 消息异常
   */
  MessageChannel<? extends Message> resolve(String id, IChannelConfig config)
      throws MessageException;
}
