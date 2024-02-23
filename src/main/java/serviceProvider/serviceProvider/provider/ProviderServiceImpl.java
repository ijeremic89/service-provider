package serviceProvider.serviceProvider.provider;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import serviceProvider.serviceProvider.exceptions.ProviderNotFoundException;
import serviceProvider.serviceProvider.exceptions.ServiceNotFoundException;
import serviceProvider.serviceProvider.service.ServiceDto;
import serviceProvider.serviceProvider.service.ServiceEntity;
import serviceProvider.serviceProvider.service.ServiceRepository;

@Service
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;
    private final ServiceRepository serviceRepository;

    public ProviderServiceImpl(ProviderRepository providerRepository, ServiceRepository serviceRepository) {
        this.providerRepository = providerRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public ProviderDto findProviderById(Long id) {
        return mapEntityToDto(providerRepository.findById(id)
                                                .orElseThrow(() -> new ProviderNotFoundException(id)));
    }

    @Override
    public List<ProviderDto> findAllProviders() {
        return providerRepository.findAll()
                                 .stream()
                                 .map(this::mapEntityToDto)
                                 .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ProviderDto createProvider(ProviderDto providerDto) {
        ProviderEntity provider = new ProviderEntity();
        mapDtoToEntity(providerDto, provider);
        return mapEntityToDto(providerRepository.save(provider));
    }

    @Transactional
    @Override
    public ProviderDto updateProvider(Long id, ProviderDto providerDto) {
        ProviderEntity provider = providerRepository.findById(id)
                                                    .orElseThrow(() -> new ProviderNotFoundException(id));

        provider.setName(providerDto.getName());

        updateProviderServices(provider, providerDto.getServices());
        ProviderEntity savedProvider = providerRepository.save(provider);
        return mapEntityToDto(savedProvider);
    }

    private void updateProviderServices(ProviderEntity provider, Set<ServiceDto> newServiceDtos) {
        Set<ServiceEntity> newServices = newServiceDtos.stream()
                                                       .map(serviceDto ->
                                                           serviceRepository.findById(serviceDto.getId())
                                                                            .orElseThrow(() -> new ServiceNotFoundException(serviceDto.getId())))
                                                       .collect(Collectors.toSet());

        // Remove services not present in the new set
        provider.getServices()
                .removeIf(existingService -> !newServices.contains(existingService));

        // Add new services not already present
        newServices.forEach(newService -> {
            if (!provider.getServices().contains(newService)) {
                provider.addService(newService);
            }
        });
    }

    @Transactional
    @Override
    public String deleteProvider(Long id) {
        ProviderEntity provider = providerRepository.findById(id)
                                                    .orElseThrow(() -> new ProviderNotFoundException(id));

        provider.removeServices();
        providerRepository.deleteById(provider.getId());
        return "Provider with id: " + id + " deleted successfully!";
    }

    private void mapDtoToEntity(ProviderDto providerDto, ProviderEntity provider) {
        provider.setName(providerDto.getName());
        if (provider.getServices() == null) {
            provider.setServices(new HashSet<>());
        }

        providerDto.getServices()
                   .forEach(_service -> {
                       ServiceEntity service =
                           serviceRepository.findById(_service.getId())
                                            .orElseThrow(() -> new ServiceNotFoundException(_service.getId()));
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
