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
package cn.asany.security.oauth.service;

import cn.asany.TestApplication;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.security.auth.oauth2.core.OAuth2AccessToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class DataBaseTokenStoreTest {

  @Autowired private DataBaseTokenStore tokenStore;

  @Test
  void storeAccessToken() {
    String token =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjEsIm5vbmNlIjoidURuMERCaWRIWlpBYms3aFduektoVVZ4NkdSV2IxR3kiLCJuYW1lIjpudWxsLCJ0b2tlbl90eXBlIjoiU0VTU0lPTiIsImNsaWVudF9pZCI6IjYwNjg0ODUzMzJjNWZjODUzYTY1IiwiZXhwaXJlc19hdCI6IjIwMjEtMTItMDZUMTQ6MzM6MjQuMDM4WiJ9.R_0EyXc4wjfL6l-TSrmvKWoGQDJp3q0m-ntC9YOu8Lk";
    OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
    assert accessToken != null;
  }
}
