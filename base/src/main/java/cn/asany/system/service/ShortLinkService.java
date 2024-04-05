package cn.asany.system.service;

import cn.asany.base.utils.Base62;
import cn.asany.system.dao.ShortLinkDao;
import cn.asany.system.domain.ShortLink;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.mybatis.keygen.util.DataBaseKeyGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShortLinkService {

  private final ShortLinkDao shortLinkDao;

  private final DataBaseKeyGenerator dataBaseKeyGenerator;

  public ShortLinkService(DataBaseKeyGenerator dataBaseKeyGenerator, ShortLinkDao shortLinkDao) {
    this.dataBaseKeyGenerator = dataBaseKeyGenerator;
    this.shortLinkDao = shortLinkDao;
  }

  @Transactional(readOnly = true)
  public Optional<ShortLink> findByCode(String code) {
    return this.shortLinkDao.findOneBy("code", code);
  }

  @Transactional(readOnly = true)
  public Page<ShortLink> findPage(Pageable pageable, PropertyFilter filter) {
    return this.shortLinkDao.findPage(pageable, filter);
  }

  @Transactional(readOnly = true)
  public Optional<ShortLink> findById(Long id) {
    return this.shortLinkDao.findById(id);
  }

  public String generateShortLinkCode(Long id) {
    return Base62.encode(id);
  }

  private long getNextId() {
    return dataBaseKeyGenerator.nextValue("sys_short_link:id");
  }

  @Transactional
  public List<ShortLink> generateShortLinks(List<ShortLink> shortLinks) {
    this.shortLinkDao.updateAllInBatch(
        shortLinks.stream()
            .peek(
                shortLink -> {
                  shortLink.setId(getNextId());
                  shortLink.setCode(generateShortLinkCode(shortLink.getId()));
                  shortLink.setAccessCount(0L);
                })
            .collect(Collectors.toList()));
    return shortLinks;
  }

  public void increaseAccessCount(ShortLink shortLink) {
    shortLink.setAccessCount(shortLink.getAccessCount() + 1);
    shortLinkDao.save(shortLink);
  }

  @Transactional
  public ShortLink update(Long id, ShortLink shortLink, boolean merge) {
    shortLink.setId(id);
    return this.shortLinkDao.update(shortLink, merge);
  }

  @Transactional
  public Optional<ShortLink> delete(Long id) {
    return this.shortLinkDao
        .findById(id)
        .map(
            item -> {
              this.shortLinkDao.delete(item);
              return item;
            });
  }

  @Transactional
  public int deleteMany(PropertyFilter filter) {
    List<ShortLink> shortLinks = this.shortLinkDao.findAll(filter);
    this.shortLinkDao.deleteAll(shortLinks);
    return shortLinks.size();
  }
}
