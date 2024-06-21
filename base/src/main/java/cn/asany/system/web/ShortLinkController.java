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
package cn.asany.system.web;

import cn.asany.system.domain.ShortLink;
import cn.asany.system.service.ShortLinkService;
import java.util.Optional;
import net.asany.jfantasy.framework.error.ValidationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ShortLinkController {

  private final ShortLinkService shortLinkService;

  public ShortLinkController(ShortLinkService shortLinkService) {
    this.shortLinkService = shortLinkService;
  }

  @GetMapping("/s/{shortCode}")
  public RedirectView redirectToOriginalUrl(@PathVariable String shortCode) {
    Optional<ShortLink> shortLinkOptional = this.shortLinkService.findByCode(shortCode);
    ShortLink shortLink =
        shortLinkOptional.orElseThrow(() -> new ValidationException("Invalid short code"));
    if (shortLink.getExpiresAt() != null && shortLink.getExpiresAt().before(new java.util.Date())) {
      throw new ValidationException("Short link expired");
    }
    if (shortLink.getUrl() == null) {
      throw new ValidationException("Short link not found");
    }
    this.shortLinkService.increaseAccessCount(shortLink);
    String originalUrl = shortLink.getUrl();
    return new RedirectView(originalUrl);
  }
}
