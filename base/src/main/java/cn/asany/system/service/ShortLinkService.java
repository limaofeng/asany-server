package cn.asany.system.service;

import cn.asany.base.utils.Base62;
import cn.asany.system.dao.ShortLinkDao;
import cn.asany.system.domain.ShortLink;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.mybatis.keygen.util.DataBaseKeyGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ShortLinkService {

  private final ShortLinkDao shortLinkDao;

  private final DataBaseKeyGenerator dataBaseKeyGenerator;

  public ShortLinkService(DataBaseKeyGenerator dataBaseKeyGenerator, ShortLinkDao shortLinkDao) {
    this.dataBaseKeyGenerator = dataBaseKeyGenerator;
    this.shortLinkDao = shortLinkDao;
  }

  public Optional<ShortLink> findByCode(String code) {
    return this.shortLinkDao.findOneBy("code", code);
  }

  public Page<ShortLink> findPage(Pageable pageable, PropertyFilter filter) {
    return this.shortLinkDao.findPage(pageable, filter);
  }

  public Optional<ShortLink> findById(Long id) {
    return this.shortLinkDao.findById(id);
  }

  public String generateShortLinkCode(Long id) {
    return Base62.encode(id);
  }

  private long getNextId() {
    return dataBaseKeyGenerator.nextValue("sys_short_link:id");
  }

  public List<ShortLink> generateEmptyShortLinks(String category, int count) {
    List<ShortLink> shortLinks = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      ShortLink shortLink = new ShortLink();
      shortLink.setId(getNextId());
      shortLink.setCode(generateShortLinkCode(shortLink.getId()));
      shortLink.setCategory(category);
      shortLinks.add(shortLink);
      shortLinkDao.save(shortLink);
    }
    return shortLinks;
  }

  public void increaseAccessCount(ShortLink shortLink) {
    shortLink.setAccessCount(shortLink.getAccessCount() + 1);
    shortLinkDao.save(shortLink);
  }

  public ShortLink generateShortLink(String url, String category, Date expiresAt) {
    Long id = getNextId();
    return this.shortLinkDao.save(
        ShortLink.builder()
            .id(id)
            .code(generateShortLinkCode(id))
            .category(category)
            .expiresAt(expiresAt)
            .url(url)
            .accessCount(0L)
            .build());
  }

  public ShortLink update(Long id, ShortLink shortLink, boolean merge) {
    shortLink.setId(id);
    return this.shortLinkDao.update(shortLink, merge);
  }

  public Optional<ShortLink> delete(Long id) {
    return this.shortLinkDao
        .findById(id)
        .map(
            item -> {
              this.shortLinkDao.delete(item);
              return item;
            });
  }

  public int deleteMany(PropertyFilter filter) {
    List<ShortLink> shortLinks = this.shortLinkDao.findAll(filter);
    this.shortLinkDao.deleteAllInBatch(shortLinks);
    return shortLinks.size();
  }
}
