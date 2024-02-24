package serviceProvider.serviceProvider.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import serviceProvider.serviceProvider.exceptions.ServiceNotFoundException;
import serviceProvider.serviceProvider.mapper.ServiceMapper;
import serviceProvider.serviceProvider.provider.ProviderRepository;
import serviceProvider.serviceProvider.provider.ProviderServiceImpl;
import serviceProvider.serviceProvider.provider.model.ProviderEntity;
import serviceProvider.serviceProvider.provider.model.ProviderWithoutServicesDTO;
import serviceProvider.serviceProvider.service.model.ServiceDTO;
import serviceProvider.serviceProvider.service.model.ServiceEntity;
import serviceProvider.serviceProvider.service.projection.ServiceDescriptionProjection;

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

    @Transactional
    @Override
    public ServiceDTO createService(ServiceDTO serviceDTO) {
        log.info("Creating service: {}", serviceDTO);
        ServiceEntity service = ServiceMapper.INSTANCE.serviceDtoToServiceEntity(serviceDTO);
        updateServiceProviders(service, serviceDTO.getProviders());
        ServiceEntity savedService = serviceRepository.save(service);
        return ServiceMapper.INSTANCE.serviceToServiceDto(savedService);
    }

    @Transactional
    @Override
    public ServiceDTO updateService(Long id, ServiceDTO serviceDTO) {
        log.info("Updating service with ID: {}", id);
        ServiceEntity service = serviceRepository.findByIdWithProviders(id)
                                                 .orElseThrow(() -> new ServiceNotFoundException(id));

        service.setDescription(serviceDTO.getDescription());
        updateServiceProviders(service, serviceDTO.getProviders());
        ServiceEntity savedService = serviceRepository.save(service);
        return ServiceMapper.INSTANCE.serviceToServiceDto(savedService);
    }

    @Transactional
    @Override
    public String deleteService(Long id) {
        log.info("Deleting service with ID: {}", id);
        ServiceEntity service = serviceRepository.findByIdWithProviders(id)
                                                 .orElseThrow(() -> new ServiceNotFoundException(id));

        service.removeProviders();
        serviceRepository.deleteById(service.getId());
        return "Service with id: " + id + " deleted successfully!";
    }

    @Override
    public List<ServiceDTO> findServicesByCriteria(String description, Long providerId) {
        Specification<ServiceEntity> spec = ServiceSpecifications.withDynamicQuery(description, providerId);
        List<ServiceEntity> services = serviceRepository.findAll(spec);
        return services.stream()
                       .map(ServiceMapper.INSTANCE::serviceToServiceDto)
                       .collect(Collectors.toList());
    }

    public List<String> findAllServiceDescriptions() {
        List<ServiceDescriptionProjection> descriptions = serviceRepository.findAllServiceDescriptions();
        return descriptions.stream()
                           .map(ServiceDescriptionProjection::getDescription)
                           .collect(Collectors.toList());
    }

    private void updateServiceProviders(ServiceEntity service, Set<ProviderWithoutServicesDTO> newProviderDtos) {
        Set<Long> newProviderIds = newProviderDtos.stream()
                                                  .map(ProviderWithoutServicesDTO::getId)
                                                  .collect(Collectors.toSet());
        List<ProviderEntity> currentProviders = new ArrayList<>(service.getProviders());
        List<ProviderEntity> newProviders = providerRepository.findAllById(newProviderIds);

        // Remove old providers not present in the new set
        currentProviders.forEach(currentProvider -> {
            if (!newProviders.contains(currentProvider)) {
                service.removeProvider(currentProvider);
            }
        });

        // Add new providers
        newProviders.forEach(newProvider -> {
            if (!currentProviders.contains(newProvider)) {
                log.debug("Adding service to provider: {}", newProvider);
                service.addProvider(newProvider);
            }
        });
    }
}
