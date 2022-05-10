package cn.asany.website.landing.service;

import cn.asany.base.common.bean.Geolocation;
import cn.asany.base.common.service.AddressService;
import cn.asany.openapi.apis.AmapOpenAPI;
import cn.asany.openapi.service.OpenAPIService;
import cn.asany.website.landing.bean.LandingPage;
import cn.asany.website.landing.bean.LandingPoster;
import cn.asany.website.landing.bean.LandingStore;
import cn.asany.website.landing.bean.enums.LandingPageStatus;
import cn.asany.website.landing.dao.LandingPageDao;
import cn.asany.website.landing.dao.LandingPosterDao;
import cn.asany.website.landing.dao.LandingStoreDao;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class LandingService {

  private final LandingPageDao landingPageDao;
  private final LandingPosterDao landingPosterDao;
  private final LandingStoreDao landingStoreDao;
  private final OpenAPIService openAPIService;
  private final AddressService addressService;

  public LandingService(
      LandingPageDao landingPageDao,
      LandingPosterDao landingPosterDao,
      LandingStoreDao landingStoreDao,
      OpenAPIService openAPIService,
      AddressService addressService) {
    this.landingPageDao = landingPageDao;
    this.landingPosterDao = landingPosterDao;
    this.landingStoreDao = landingStoreDao;
    this.openAPIService = openAPIService;
    this.addressService = addressService;
  }

  @Transactional(readOnly = true)
  public Pager<LandingPage> findLandingPagePager(
      Pager<LandingPage> pager, List<PropertyFilter> filter) {
    return this.landingPageDao.findPager(pager, filter);
  }

  @Transactional(readOnly = true)
  public Pager<LandingPoster> findLandingPosterPager(
      Pager<LandingPoster> pager, List<PropertyFilter> filter) {
    return this.landingPosterDao.findPager(pager, filter);
  }

  @Transactional(readOnly = true)
  public Pager<LandingStore> findLandingStorePager(
      Pager<LandingStore> pager, List<PropertyFilter> filter) {
    return this.landingStoreDao.findPager(pager, filter);
  }

  public void resolveGeolocation(LandingStore store) {
    try {
      if (store.getLocation() == null && store.getAddress() != null) {
        AmapOpenAPI openAPI = openAPIService.getDefaultAmap();
        AmapOpenAPI.Geocode geocode;
        String district = store.getAddress().getDistrict();
        String detailedAddress = store.getAddress().getDetailedAddress();
        if (StringUtils.isBlank(detailedAddress) && StringUtils.isNotBlank(district)) {
          geocode = openAPI.geocode_geo(addressService.getFullAddress(store.getAddress())).get(0);
        } else if (StringUtils.isNotBlank(detailedAddress) && StringUtils.isBlank(district)) {
          geocode = openAPI.geocode_geo(detailedAddress).get(0);
          String districtStr = geocode.getDistrict();
          addressService.loadAddress(store.getAddress(), geocode.getAdcode());
          int index = detailedAddress.indexOf(districtStr);
          if (index != -1) {
            detailedAddress = detailedAddress.substring(index + districtStr.length());
            store.getAddress().setDetailedAddress(detailedAddress);
          }
        } else {
          geocode = openAPI.geocode_geo(detailedAddress, district).get(0);
        }
        String location = geocode.getLocation();
        String lng = StringUtils.substringBefore(location, ",");
        String lat = StringUtils.substringAfter(location, ",");
        store.setLocation(
            Geolocation.builder()
                .latitude(new BigDecimal(lat))
                .longitude(new BigDecimal(lng))
                .build());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @Transactional
  public LandingStore save(LandingStore store) {
    resolveGeolocation(store);
    return this.landingStoreDao.save(store);
  }

  @Transactional
  public List<LandingStore> saveStoreAllInBath(List<LandingStore> stores) {
    for (LandingStore store : stores) {
      resolveGeolocation(store);
    }
    this.landingStoreDao.saveAllInBatch(stores);
    return stores;
  }

  @Transactional
  public LandingStore update(Long id, LandingStore store, boolean merge) {
    store.setId(id);
    resolveGeolocation(store);
    return this.landingStoreDao.update(store, merge);
  }

  @Transactional
  public int deleteStore(Long... ids) {
    this.landingStoreDao.deleteAllById(Arrays.asList(ids));
    return ids.length;
  }

  @Transactional(readOnly = true)
  public Optional<LandingStore> findStore(Long id) {
    return this.landingStoreDao.findById(id);
  }

  @Transactional(readOnly = true)
  public Optional<LandingPage> findPage(Long id) {
    return this.landingPageDao.findByIdWithPosterAndStores(id);
  }

  @Transactional(readOnly = true)
  public Optional<LandingPoster> findPoster(Long id) {
    return this.landingPosterDao.findById(id);
  }

  @Transactional
  public LandingPage save(LandingPage page) {
    page.setStatus(LandingPageStatus.DRAFT);
    return this.landingPageDao.save(page);
  }

  @Transactional
  public LandingPage update(Long id, LandingPage page, boolean merge) {
    page.setId(id);
    return this.landingPageDao.update(page, merge);
  }

  @Transactional
  public int deletePage(Long... ids) {
    this.landingPageDao.deleteAllById(Arrays.asList(ids));
    return ids.length;
  }

  @Transactional
  public LandingPoster save(LandingPoster poster) {
    return this.landingPosterDao.save(poster);
  }

  @Transactional
  public LandingPoster update(Long id, LandingPoster poster, boolean merge) {
    poster.setId(id);
    return this.landingPosterDao.update(poster, merge);
  }

  @Transactional
  public int deletePoster(Long[] ids) {
    this.landingPosterDao.deleteAllById(Arrays.asList(ids));
    return ids.length;
  }
}
