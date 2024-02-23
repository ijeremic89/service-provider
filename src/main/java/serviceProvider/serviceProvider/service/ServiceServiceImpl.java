package serviceProvider.serviceProvider.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import serviceProvider.serviceProvider.exceptions.ServiceNotFoundException;
import serviceProvider.serviceProvider.mapper.ServiceMapper;
import serviceProvider.serviceProvider.provider.ProviderRepository;
import serviceProvider.serviceProvider.provider.ProviderServiceImpl;

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
    public ServiceDto findServiceById(Long id) {
        log.info("Finding service by ID: {}", id);
        return ServiceMapper.INSTANCE.serviceToServiceDto(serviceRepository.findByIdWithProviders(id)
                                                                           .orElseThrow(() -> new ServiceNotFoundException(id)));
    }

    @Override
    public List<ServiceDto> findAllServices() {
        log.info("Fetching all services");
        return serviceRepository.findAllWithProviders()
                                .stream()
                                .map(ServiceMapper.INSTANCE::serviceToServiceDto)
                                .collect(Collectors.toList());
    }

    @Override
    public ServiceDto createService(ServiceDto serviceDto) {
        return null;
    }

    @Override
    public ServiceDto updateService(Long id, ServiceDto serviceDto) {
        return null;
    }

    @Override
    public String deleteService(Long id) {
        return null;
    }

    @Override
    public List<ServiceDto> findServicesByCriteria(String name, Long serviceId) {
        return null;
    }
}
