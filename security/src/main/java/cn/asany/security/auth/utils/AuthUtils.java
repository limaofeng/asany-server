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
package cn.asany.security.auth.utils;

import jakarta.servlet.http.HttpServletRequest;
import net.asany.jfantasy.framework.security.auth.TokenType;
import net.asany.jfantasy.framework.security.auth.oauth2.core.OAuth2Authentication;
import net.asany.jfantasy.framework.security.auth.oauth2.core.OAuth2AuthenticationDetails;
import net.asany.jfantasy.framework.security.authentication.Authentication;

public class AuthUtils {

  public static OAuth2Authentication buildOAuth2(
      String clientId, HttpServletRequest request, Authentication authentication) {
    OAuth2AuthenticationDetails oAuth2AuthenticationDetails = new OAuth2AuthenticationDetails();
    oAuth2AuthenticationDetails.setClientId(clientId);
    oAuth2AuthenticationDetails.setTokenType(TokenType.SESSION_ID);
    oAuth2AuthenticationDetails.setRequest(request);
    return new OAuth2Authentication(authentication, oAuth2AuthenticationDetails);
  }
}
