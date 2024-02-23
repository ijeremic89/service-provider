package serviceProvider.serviceProvider.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import serviceProvider.serviceProvider.service.ServiceDto;
import serviceProvider.serviceProvider.service.ServiceEntity;

@Mapper
public interface ServiceMapper {

    ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);

    ServiceDto serviceToServiceDto(ServiceEntity service);

    ServiceEntity serviceDtoToServiceEntity(ServiceDto serviceDto);
}
