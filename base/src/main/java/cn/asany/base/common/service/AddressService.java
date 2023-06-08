package cn.asany.base.common.service;

import cn.asany.base.common.domain.Address;
import cn.asany.system.domain.Dict;
import cn.asany.system.domain.DictType;
import cn.asany.system.service.DictService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

  private static final String[] ALL_TYPE =
      new String[] {
        DictType.TYPE_CODE_PCAS_PROVINCE,
        DictType.TYPE_CODE_PCAS_CITY,
        DictType.TYPE_CODE_PCAS_DISTRICT,
        DictType.TYPE_CODE_PCAS_STREET
      };

  private final DictService dictService;

  public AddressService(DictService dictService) {
    this.dictService = dictService;
  }

  public Optional<String> getCountryName(Address address) {
    Optional<Dict> optional =
        dictService.findByCode(address.getCountry(), DictType.TYPE_CODE_COUNTRY);
    return optional.map(Dict::getName);
  }

  public Optional<String> getProvinceName(Address address) {
    Optional<Dict> optional =
        dictService.findByCode(address.getProvince(), DictType.TYPE_CODE_PCAS_PROVINCE);
    return optional.map(Dict::getName);
  }

  public Optional<String> getCityName(Address address) {
    Optional<Dict> optional =
        dictService.findByCode(address.getCity(), DictType.TYPE_CODE_PCAS_CITY);
    return optional.map(Dict::getName);
  }

  public Optional<String> getDistrictName(Address address) {
    Optional<Dict> optional =
        dictService.findByCode(address.getDistrict(), DictType.TYPE_CODE_PCAS_DISTRICT);
    return optional.map(Dict::getName);
  }

  public Optional<String> getStreetName(Address address) {
    Optional<Dict> optional =
        dictService.findByCode(address.getStreet(), DictType.TYPE_CODE_PCAS_STREET);
    return optional.map(Dict::getName);
  }

  public void loadAddress(Address address, String code) {
    PropertyFilter filter = PropertyFilter.newFilter();

    filter.in("type", ALL_TYPE);
    filter.equal("code", code);

    Optional<Dict> optional = dictService.findOne(filter);
    if (!optional.isPresent()) {
      return;
    }

    String[] allCode =
        StringUtil.tokenizeToStringArray(optional.get().getPath(), Dict.PATH_SEPARATOR);

    List<Dict> dicts =
        dictService.findAll(
            PropertyFilter.newFilter().in("code", allCode).in("type", ALL_TYPE));

    for (Dict dict : dicts) {
      switch (dict.getType()) {
        case DictType.TYPE_CODE_PCAS_PROVINCE:
          address.setProvince(dict.getCode());
          break;
        case DictType.TYPE_CODE_PCAS_CITY:
          address.setCity(dict.getCode());
          break;
        case DictType.TYPE_CODE_PCAS_DISTRICT:
          address.setDistrict(dict.getCode());
          break;
        case DictType.TYPE_CODE_PCAS_STREET:
          address.setStreet(dict.getCode());
          break;
      }
    }
  }

  public String getFullAddress(Address address) {
    return this.getFullAddress(address, false);
  }

  public String getFullAddress(Address address, boolean excludeDetailed) {
    List<String> allCode =
        Arrays.stream(
                new String[] {
                  address.getProvince(),
                  address.getCity(),
                  address.getDistrict(),
                  address.getStreet()
                })
            .filter(StringUtil::isNotBlank)
            .collect(Collectors.toList());

    List<Dict> dicts =
        dictService.findAll(
            PropertyFilter.newFilter().in("code", allCode).in("type", ALL_TYPE));

    StringBuilder builder = new StringBuilder();
    for (String type : ALL_TYPE) {
      Dict dict = ObjectUtil.find(dicts, "type", type);
      if (dict == null) {
        continue;
      }
      builder.append(dict.getName());
    }

    if (!excludeDetailed && StringUtil.isNotBlank(address.getDetailedAddress())) {
      builder.append(address.getDetailedAddress());
    }

    return builder.toString();
  }
}
