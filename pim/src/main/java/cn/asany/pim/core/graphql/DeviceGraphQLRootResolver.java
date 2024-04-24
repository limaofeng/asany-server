package cn.asany.pim.core.graphql;

import cn.asany.base.common.BatchPayload;
import cn.asany.pim.core.convert.DeviceConverter;
import cn.asany.pim.core.domain.Device;
import cn.asany.pim.core.graphql.input.DeviceCreateInput;
import cn.asany.pim.core.graphql.input.DeviceUpdateInput;
import cn.asany.pim.core.graphql.input.DeviceWhereInput;
import cn.asany.pim.core.graphql.type.DeviceConnection;
import cn.asany.pim.core.graphql.type.DeviceIdType;
import cn.asany.pim.core.service.DeviceService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class DeviceGraphQLRootResolver implements GraphQLMutationResolver, GraphQLQueryResolver {

  private final DeviceService deviceService;
  private final DeviceConverter deviceConverter;

  public DeviceGraphQLRootResolver(DeviceService deviceService, DeviceConverter deviceConverter) {
    this.deviceService = deviceService;
    this.deviceConverter = deviceConverter;
  }

  public DeviceConnection devicesConnection(
      DeviceWhereInput where, int pageNumber, int pageSize, Sort orderBy) {
    Page<Device> page =
        this.deviceService.findPage(
            PageRequest.of(pageNumber - 1, pageSize, orderBy), where.toFilter());
    return Kit.connection(page, DeviceConnection.class);
  }

  public List<Device> devices(DeviceWhereInput where, int offset, int limit, Sort sort) {
    return this.deviceService.findAll(where.toFilter(), offset, limit, sort);
  }

  public Optional<Device> device(String id, DeviceIdType type) {
    if (DeviceIdType.ID == type) {
      return this.deviceService.findById(Long.valueOf(id));
    } else if (DeviceIdType.NO == type) {
      return this.deviceService.findByNO(id);
    }
    return this.deviceService.findBySN(id);
  }

  public Device createDevice(DeviceCreateInput input) {
    Device device = deviceConverter.toDevice(input);
    return this.deviceService.save(device);
  }

  public Device updateDevice(Long id, DeviceUpdateInput input, Boolean merge) {
    Device device = deviceConverter.toDevice(input);
    return this.deviceService.update(id, device, merge);
  }

  public Optional<Device> deleteDevice(Long id) {
    return this.deviceService.delete(id);
  }

  public BatchPayload deleteManyDevices(DeviceWhereInput where) {
    return BatchPayload.of(this.deviceService.deleteMany(where.toFilter()));
  }
}
