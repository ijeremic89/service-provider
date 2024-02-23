package serviceProvider.serviceProvider.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import serviceProvider.serviceProvider.provider.model.ProviderWithoutServicesDTO;
import serviceProvider.serviceProvider.service.model.ServiceDTO;
import serviceProvider.serviceProvider.service.model.ServiceEntity;

@Mapper
public interface ServiceMapper {

    ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);

    @Mapping(target = "providers", ignore = true)
    ServiceDTO serviceToServiceDto(ServiceEntity service);

    @AfterMapping
    default void fillProviders(ServiceEntity service, @MappingTarget ServiceDTO serviceDto) {
        if (service.getProviders() != null) {
            Set<ProviderWithoutServicesDTO> providers = service.getProviders()
                                                               .stream()
                                                               .map(provider -> new ProviderWithoutServicesDTO(provider.getId(), provider.getName()))
                                                               .collect(Collectors.toSet());
            serviceDto.setProviders(providers);
        }
    }

    @Mapping(target = "providers", ignore = true)
    ServiceEntity serviceDtoToServiceEntity(ServiceDTO serviceDTO);
}
