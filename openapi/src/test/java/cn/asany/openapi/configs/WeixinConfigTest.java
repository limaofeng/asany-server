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
package cn.asany.openapi.configs;

import static org.junit.jupiter.api.Assertions.*;

import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.jackson.JSON;
import org.junit.jupiter.api.Test;

@Slf4j
class WeixinConfigTest {

  @Test
  void print() {
    WeixinConfig config =
        WeixinConfig.builder()
            .appId("wx59114ee8f0ca3806")
            .appSecret("c5f6bd30f31c051155ac3bba094b423f")
            .build();
    log.debug(JSON.serialize(config));
  }
}
