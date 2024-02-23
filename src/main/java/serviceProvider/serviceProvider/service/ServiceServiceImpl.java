package serviceProvider.serviceProvider.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public List<ServiceDTO> findServicesByCriteria(String name, Long serviceId) {
        return null;
    }

    private void updateServiceProviders(ServiceEntity service, Set<ProviderWithoutServicesDTO> newProviderDtos) {
        Set<Long> newProviderIds = newProviderDtos.stream()
                                                  .map(ProviderWithoutServicesDTO::getId)
                                                  .collect(Collectors.toSet());
        List<ProviderEntity> currentProviders = new ArrayList<>(service.getProviders());
        List<ProviderEntity> newProviders = providerRepository.findAllById(newProviderIds);

        // Remove old providers not present in the new set
        for (ProviderEntity currentProvider : currentProviders) {
            if (!newProviderIds.contains(currentProvider.getId())) {
                service.removeProvider(currentProvider); // This also takes care of the other side of the relationship
            }
        }

        // Add new providers
        for (ProviderEntity newProvider : newProviders) {
            if (!service.getProviders().contains(newProvider)) {
                service.addProvider(newProvider); // This also takes care of the other side of the relationship
            }
        }
    }
}
