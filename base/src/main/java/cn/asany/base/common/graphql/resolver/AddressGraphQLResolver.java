package cn.asany.base.common.graphql.resolver;

import cn.asany.base.common.domain.Address;
import cn.asany.base.common.service.AddressService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class AddressGraphQLResolver implements GraphQLResolver<Address> {

  private final AddressService addressService;

  public AddressGraphQLResolver(AddressService addressService) {
    this.addressService = addressService;
  }

  public Optional<String> countryName(Address address) {
    return addressService.getCountryName(address);
  }

  public Optional<String> provinceName(Address address) {
    return addressService.getProvinceName(address);
  }

  public Optional<String> cityName(Address address) {
    return addressService.getCityName(address);
  }

  public Optional<String> districtName(Address address) {
    return addressService.getDistrictName(address);
  }

  public Optional<String> streetName(Address address) {
    return addressService.getStreetName(address);
  }

  public String fullAddress(Address address, boolean excludeDetailed) {
    return addressService.getFullAddress(address, excludeDetailed);
  }
}
