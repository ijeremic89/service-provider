package serviceProvider.serviceProvider.service;

import java.util.List;

public interface ServiceService {
    ServiceDto findServiceById(Long id);

    List<ServiceDto> findAllServices();

    ServiceDto createService(ServiceDto serviceDto);

    ServiceDto updateService(Long id, ServiceDto serviceDto);

    String deleteService(Long id);

    List<ServiceDto> findServicesByCriteria(String name, Long serviceId);
}
