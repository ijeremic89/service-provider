package serviceProvider.serviceProvider.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import serviceProvider.serviceProvider.exceptions.ServiceNotFoundException;
import serviceProvider.serviceProvider.mapper.ServiceMapper;
import serviceProvider.serviceProvider.provider.ProviderRepository;
import serviceProvider.serviceProvider.provider.ProviderServiceImpl;
import serviceProvider.serviceProvider.provider.model.ProviderEntity;
import serviceProvider.serviceProvider.provider.model.ProviderWithoutServicesDTO;
import serviceProvider.serviceProvider.service.model.ServiceDTO;
import serviceProvider.serviceProvider.service.model.ServiceEntity;

@Service
public class ServiceServiceImpl implements ServiceService {

    private static final Logger log = LoggerFactory.getLogger(ProviderServiceImpl.class);

    private final ServiceRepository serviceRepository;
    private final ProviderRepository providerRepository;

    public ServiceServiceImpl(ServiceRepository serviceRepository, ProviderRepository providerRepository) {
        this.serviceRepository = serviceRepository;
        this.providerRepository = providerRepository;
    }

    @Override
    public ServiceDTO findServiceById(Long id) {
        log.info("Finding service by ID: {}", id);
        return ServiceMapper.INSTANCE.serviceToServiceDto(serviceRepository.findByIdWithProviders(id)
                                                                           .orElseThrow(() -> new ServiceNotFoundException(id)));
    }

    @Override
    public List<ServiceDTO> findAllServices() {
        log.info("Fetching all services");
        return serviceRepository.findAllWithProviders()
                                .stream()
                                .map(ServiceMapper.INSTANCE::serviceToServiceDto)
                                .collect(Collectors.toList());
    }

    @Override
    public ServiceDTO createService(ServiceDTO serviceDTO) {
        log.info("Creating service: {}", serviceDTO);
        ServiceEntity service = ServiceMapper.INSTANCE.serviceDtoToServiceEntity(serviceDTO);
        updateServiceProviders(service, serviceDTO.getProviders());
        ServiceEntity savedService = serviceRepository.save(service);
        return ServiceMapper.INSTANCE.serviceToServiceDto(savedService);
    }

    @Override
    public ServiceDTO updateService(Long id, ServiceDTO serviceDTO) {
        return null;
    }

    @Override
    public String deleteService(Long id) {
        return null;
    }

    @Override
    public List<ServiceDTO> findServicesByCriteria(String name, Long serviceId) {
        return null;
    }

    private void updateServiceProviders(ServiceEntity service, Set<ProviderWithoutServicesDTO> newProviderDtos) {
        Set<Long> newProviderIds = newProviderDtos.stream()
                                                  .map(ProviderWithoutServicesDTO::getId)
                                                  .collect(Collectors.toSet());
        List<ProviderEntity> newProviders = providerRepository.findAllById(newProviderIds);

        service.getProviders()
               .removeIf(existingProvider -> !newProviders.contains(existingProvider));

        newProviders.forEach(newProvider -> {
            if (!service.getProviders().contains(newProvider)) {
                log.debug("Adding service to provider: {}", newProviders);
                service.addProvider(newProvider);
            }
        });
    }
}
