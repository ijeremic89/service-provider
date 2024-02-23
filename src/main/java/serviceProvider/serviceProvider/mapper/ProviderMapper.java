package serviceProvider.serviceProvider.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import serviceProvider.serviceProvider.provider.ProviderDto;
import serviceProvider.serviceProvider.provider.ProviderEntity;

@Mapper(uses = ServiceMapper.class)
public interface ProviderMapper {
    ProviderMapper INSTANCE = Mappers.getMapper(ProviderMapper.class);

    @Mapping(target = "services", source = "services")
    ProviderDto providerToProviderDto(ProviderEntity provider);

    @Mapping(target = "services", ignore = true)
    ProviderEntity providerDtoToProviderEntity(ProviderDto providerDto);
}
