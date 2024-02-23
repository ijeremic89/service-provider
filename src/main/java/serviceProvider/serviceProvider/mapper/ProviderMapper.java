package serviceProvider.serviceProvider.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import serviceProvider.serviceProvider.provider.model.ProviderDTO;
import serviceProvider.serviceProvider.provider.model.ProviderEntity;
import serviceProvider.serviceProvider.service.model.ServiceWithoutProvidersDTO;

@Mapper(uses = ServiceMapper.class)
public interface ProviderMapper {

    ProviderMapper INSTANCE = Mappers.getMapper(ProviderMapper.class);

    @Mapping(target = "services", ignore = true)
    ProviderDTO providerToProviderDto(ProviderEntity provider);

    @AfterMapping
    default void fillServices(ProviderEntity provider, @MappingTarget ProviderDTO providerDTO) {
        if (provider.getServices() != null) {
            Set<ServiceWithoutProvidersDTO> services = provider.getServices()
                                                               .stream()
                                                               .map(service -> new ServiceWithoutProvidersDTO(service.getId(), service.getDescription()))
                                                               .collect(Collectors.toSet());
            providerDTO.setServices(services);
        }
    }

    @Mapping(target = "services", ignore = true)
    ProviderEntity providerDtoToProviderEntity(ProviderDTO providerDTO);
}
