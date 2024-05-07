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
package cn.asany.message.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cn.asany.message.api.EmailChannelConfig;
import cn.asany.message.api.EmailMessage;
import cn.asany.message.api.MessageException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderFactory;
import org.junit.jupiter.api.Test;

@Slf4j
class EmailMessageChannelTest {

  @Test
  void testAsync() {
    Executor executor = Executors.newFixedThreadPool(10);
    DataLoader<String, String> dataLoader =
        DataLoaderFactory.newDataLoader(
            (ids) ->
                CompletableFuture.supplyAsync(
                    () -> ids.stream().map(id -> "loaded: " + id).collect(Collectors.toList()),
                    executor));
    CompletableFuture<Void> testFuture =
        dataLoader
            .load("1")
            .thenAccept(
                result -> {
                  assertEquals("loaded: 1", result);
                });
    dataLoader.dispatch();
    testFuture.join(); // 等待异步处理完成
  }

  @Test
  void send() throws MessageException {
    EmailMessageChannel email =
        new EmailMessageChannel(
            EmailChannelConfig.builder()
                .host("*******")
                .port(465)
                .username("*******")
                .password("*****")
                .from("*******")
                .fromName("服务中心")
                .build());
    email.send(
        EmailMessage.builder()
            .to("李茂峰<limaofeng@msn.com>")
            .text("<h1>Hello</h1><p>This is a HTML email!</p>")
            .subject("测试邮件")
            .build());
  }
}
