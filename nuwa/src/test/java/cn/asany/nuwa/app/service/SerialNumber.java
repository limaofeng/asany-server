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
package cn.asany.nuwa.app.service;

import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.junit.jupiter.api.Test;

@Slf4j
public class SerialNumber {
  @Test
  void generateClientSecret() {
    String clientId = StringUtil.generateNonceString(ApplicationService.NONCE_CHARS, 20);
    String clientSecretStr = StringUtil.generateNonceString(ApplicationService.NONCE_CHARS, 40);
    log.debug("clientId: " + clientId);
    log.debug("clientSecret: " + clientSecretStr);
  }
}
