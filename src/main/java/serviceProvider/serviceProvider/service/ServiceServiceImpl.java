package serviceProvider.serviceProvider.service;

import java.util.List;

import org.springframework.stereotype.Service;

import serviceProvider.serviceProvider.provider.ProviderRepository;

@Service
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final ProviderRepository providerRepository;

    public ServiceServiceImpl(ServiceRepository serviceRepository, ProviderRepository providerRepository) {
        this.serviceRepository = serviceRepository;
        this.providerRepository = providerRepository;
    }

    @Override
    public ServiceDto findServiceById(Long id) {
        return null;
    }

    @Override
    public List<ServiceDto> findAllServices() {
        return null;
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
