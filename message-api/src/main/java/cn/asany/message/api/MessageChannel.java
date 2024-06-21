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
 * 消息发送者
 *
 * @author limaofeng
 */
public interface MessageChannel<T extends Message> {

  /**
   * 发送消息
   *
   * @param message 消息
   * @throws MessageException 消息异常
   */
  void send(T message) throws MessageException;
}
