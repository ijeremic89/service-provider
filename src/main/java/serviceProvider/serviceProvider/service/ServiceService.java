package serviceProvider.serviceProvider.service;

import java.util.List;

import serviceProvider.serviceProvider.service.model.ServiceDTO;

public interface ServiceService {

    ServiceDTO findServiceById(Long id);

    List<ServiceDTO> findAllServices();

    ServiceDTO createService(ServiceDTO serviceDTO);

    ServiceDTO updateService(Long id, ServiceDTO serviceDTO);

    String deleteService(Long id);

    List<ServiceDTO> findServicesByCriteria(String name, Long serviceId);
}
