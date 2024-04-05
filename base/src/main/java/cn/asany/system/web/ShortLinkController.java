package cn.asany.system.web;

import cn.asany.system.domain.ShortLink;
import cn.asany.system.service.ShortLinkService;
import java.util.Optional;
import org.jfantasy.framework.error.ValidationException;
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
