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
import serviceProvider.serviceProvider.provider.model.ProviderDTO;
import serviceProvider.serviceProvider.provider.model.ProviderEntity;
import serviceProvider.serviceProvider.service.ServiceRepository;
import serviceProvider.serviceProvider.service.model.ServiceEntity;
import serviceProvider.serviceProvider.service.model.ServiceWithoutProvidersDTO;

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
    public ProviderDTO findProviderById(Long id) {
        log.info("Finding provider by ID: {}", id);
        return ProviderMapper.INSTANCE.providerToProviderDto(providerRepository.findByIdWithServices(id)
                                                                               .orElseThrow(() -> new ProviderNotFoundException(id)));
    }

    @Override
    public List<ProviderDTO> findAllProviders() {
        log.info("Fetching all providers");
        return providerRepository.findAllWithServices()
                                 .stream()
                                 .map(ProviderMapper.INSTANCE::providerToProviderDto)
                                 .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ProviderDTO createProvider(ProviderDTO providerDTO) {
        log.info("Creating provider: {}", providerDTO);
        ProviderEntity provider = ProviderMapper.INSTANCE.providerDtoToProviderEntity(providerDTO);
        updateProviderServices(provider, providerDTO.getServices());
        ProviderEntity savedProvider = providerRepository.save(provider);
        return ProviderMapper.INSTANCE.providerToProviderDto(savedProvider);
    }

    @Transactional
    @Override
    public ProviderDTO updateProvider(Long id, ProviderDTO providerDTO) {
        log.info("Updating provider with ID: {}", id);
        ProviderEntity provider = providerRepository.findByIdWithServices(id)
                                                    .orElseThrow(() -> new ProviderNotFoundException(id));

        provider.setName(providerDTO.getName());
        updateProviderServices(provider, providerDTO.getServices());
        ProviderEntity savedProvider = providerRepository.save(provider);
        return ProviderMapper.INSTANCE.providerToProviderDto(savedProvider);
    }

    @Transactional
    @Override
    public String deleteProvider(Long id) {
        log.info("Deleting provider with ID: {}", id);
        ProviderEntity provider = providerRepository.findByIdWithServices(id)
                                                    .orElseThrow(() -> new ProviderNotFoundException(id));

        provider.removeServices();
        providerRepository.deleteById(provider.getId());
        return "Provider with id: " + id + " deleted successfully!";
    }

    @Override
    public List<ProviderDTO> findProvidersByCriteria(String name, Long serviceId) {
        Specification<ProviderEntity> spec = ProviderSpecifications.withDynamicQuery(name, serviceId);
        List<ProviderEntity> providers = providerRepository.findAll(spec);
        return providers.stream()
                        .map(ProviderMapper.INSTANCE::providerToProviderDto)
                        .collect(Collectors.toList());
    }

    private void updateProviderServices(ProviderEntity provider, Set<ServiceWithoutProvidersDTO> newServiceDtos) {
        Set<Long> newServiceIds = newServiceDtos.stream()
                                                .map(ServiceWithoutProvidersDTO::getId)
                                                .collect(Collectors.toSet());
        Set<ServiceEntity> currentServices = provider.getServices();
        List<ServiceEntity> newServices = serviceRepository.findAllById(newServiceIds);

        // Remove old services not present in the new set
        currentServices.forEach(currentService -> {
            if (!newServices.contains(currentService)) {
                provider.removeService(currentService);
            }
        });

        // Add new services
        newServices.forEach(newService -> {
            if (!currentServices.contains(newService)) {
                log.debug("Adding service to provider: {}", newService);
                provider.addService(newService);
            }
        });
    }
}
