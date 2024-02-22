package serviceProvider.serviceProvider.provider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import serviceProvider.serviceProvider.exceptions.ProviderNotFoundException;
import serviceProvider.serviceProvider.service.ServiceDto;
import serviceProvider.serviceProvider.service.ServiceEntity;
import serviceProvider.serviceProvider.service.ServiceRepository;

@Service
public class ProviderServiceImpl implements ProviderService {

    @Resource
    private ProviderRepository providerRepository;

    @Resource
    private ServiceRepository serviceRepository;

    @Override
    public ProviderDto findProviderById(Long id) {
        return mapEntityToDto(providerRepository.findById(id)
                                                .orElseThrow(() -> new ProviderNotFoundException(id)));
    }

    @Override
    public List<ProviderDto> findAllProviders() {
        List<ProviderEntity> providerEntities = providerRepository.findAll();
        List<ProviderDto> providers = new ArrayList<>();

        providerEntities.forEach(provider -> {
            ProviderDto providerDto = mapEntityToDto(provider);
            providers.add(providerDto);
        });
        return providers;
    }

    @Override
    public ProviderDto createProvider(ProviderDto providerDto) {
        ProviderEntity provider = new ProviderEntity();
        mapDtoToEntity(providerDto, provider);
        return mapEntityToDto(providerRepository.save(provider));
    }

    @Override
    public ProviderDto updateProvider(Long id, ProviderDto providerDto) {
        ProviderEntity provider = providerRepository.findById(id)
                                                    .orElseThrow(() -> new ProviderNotFoundException(id));

        providerDto.setId(provider.getId());
        provider.getServices().clear();
        mapDtoToEntity(providerDto, provider);
        ProviderEntity savedProvider = providerRepository.save(provider);
        return mapEntityToDto(savedProvider);
    }

    @Override
    public String deleteProvider(Long id) {
        ProviderEntity provider = providerRepository.findById(id)
                                                    .orElseThrow(() -> new ProviderNotFoundException(id));

        provider.removeServices();
        providerRepository.deleteById(provider.getId());
        return "Provider with id: " + id + " deleted successfully!";
    }

    private void mapDtoToEntity(ProviderDto providerDto, ProviderEntity provider) {
        provider.setId(providerDto.getId());
        provider.setName(providerDto.getName());
        if (provider.getServices() == null) {
            provider.setServices(new HashSet<>());
        }

        providerDto.getServices()
                   .forEach(_service -> {
                       ServiceEntity service =
                           serviceRepository.findById(_service.getId()).orElseThrow(() -> new ProviderNotFoundException(2L)); //TODO: add service service
                       if (service != null) {
                           provider.addService(service);
                       }
                   });
    }

    private ProviderDto mapEntityToDto(ProviderEntity provider) {
        ProviderDto providerDto = new ProviderDto();
        providerDto.setId(provider.getId());
        providerDto.setName(provider.getName());
        providerDto.setServices(provider.getServices()
                                        .stream()
                                        .map(service -> new ServiceDto(service.getId(), service.getDescription()))
                                        .collect(Collectors.toSet()));
        return providerDto;
    }
}
