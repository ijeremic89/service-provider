package serviceProvider.serviceProvider.provider;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import serviceProvider.serviceProvider.exceptions.ProviderNotFoundException;
import serviceProvider.serviceProvider.mapper.ProviderMapper;
import serviceProvider.serviceProvider.service.ServiceDto;
import serviceProvider.serviceProvider.service.ServiceEntity;
import serviceProvider.serviceProvider.service.ServiceRepository;

@Service
public class ProviderServiceImpl implements ProviderService {

    private static final Logger log = LoggerFactory.getLogger(ProviderServiceImpl.class);

    private final ProviderRepository providerRepository;
    private final ServiceRepository serviceRepository;

    public ProviderServiceImpl(ProviderRepository providerRepository, ServiceRepository serviceRepository) {
        this.providerRepository = providerRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public ProviderDto findProviderById(Long id) {
        log.info("Finding provider by ID: {}", id);
        return ProviderMapper.INSTANCE.providerToProviderDto(providerRepository.findById(id)
                                                                               .orElseThrow(() -> new ProviderNotFoundException(id)));
    }

    @Override
    public List<ProviderDto> findAllProviders() {
        log.info("Fetching all providers");
        return providerRepository.findAll()
                                 .stream()
                                 .map(ProviderMapper.INSTANCE::providerToProviderDto)
                                 .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ProviderDto createProvider(ProviderDto providerDto) {
        log.info("Creating provider: {}", providerDto);
        ProviderEntity provider = ProviderMapper.INSTANCE.providerDtoToProviderEntity(providerDto);
        updateProviderServices(provider, providerDto.getServices());
        ProviderEntity savedProvider = providerRepository.save(provider);
        return ProviderMapper.INSTANCE.providerToProviderDto(savedProvider);
    }

    @Transactional
    @Override
    public ProviderDto updateProvider(Long id, ProviderDto providerDto) {
        log.info("Updating provider with ID: {}", id);
        ProviderEntity provider = providerRepository.findById(id)
                                                    .orElseThrow(() -> new ProviderNotFoundException(id));

        ProviderMapper.INSTANCE.providerDtoToProviderEntity(providerDto);
        updateProviderServices(provider, providerDto.getServices());
        ProviderEntity savedProvider = providerRepository.save(provider);
        return ProviderMapper.INSTANCE.providerToProviderDto(savedProvider);
    }

    @Transactional
    @Override
    public String deleteProvider(Long id) {
        log.info("Deleting provider with ID: {}", id);
        ProviderEntity provider = providerRepository.findById(id)
                                                    .orElseThrow(() -> new ProviderNotFoundException(id));

        provider.removeServices();
        providerRepository.deleteById(provider.getId());
        return "Provider with id: " + id + " deleted successfully!";
    }

    @Override
    public List<ProviderDto> findProvidersByCriteria(String name, Long serviceId) {
        Specification<ProviderEntity> spec = ProviderSpecifications.withDynamicQuery(name, serviceId);
        List<ProviderEntity> providers = providerRepository.findAll(spec);
        return providers.stream()
                        .map(ProviderMapper.INSTANCE::providerToProviderDto)
                        .collect(Collectors.toList());
    }

    private void updateProviderServices(ProviderEntity provider, Set<ServiceDto> newServiceDtos) {
        Set<Long> newServiceIds = newServiceDtos.stream()
                                                .map(ServiceDto::getId)
                                                .collect(Collectors.toSet());
        List<ServiceEntity> newServices = serviceRepository.findAllById(newServiceIds);

        provider.getServices()
                .removeIf(existingService -> !newServices.contains(existingService));

        newServices.forEach(newService -> {
            if (!provider.getServices().contains(newService)) {
                log.debug("Adding service to provider: {}", newService);
                provider.addService(newService);
            }
        });
    }
}
