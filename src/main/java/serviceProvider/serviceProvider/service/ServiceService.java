package serviceProvider.serviceProvider.service;

import java.util.List;

import serviceProvider.serviceProvider.service.model.ServiceDTO;

public interface ServiceService {

    /**
     * Retrieves a service by its unique identifier.
     *
     * @param id The unique identifier of the service to retrieve.
     * @return A {@link ServiceDTO} representing the service with the specified ID. Returns {@code null} if no service is found.
     */
    ServiceDTO findServiceById(Long id);

    /**
     * Retrieves a list of all available services.
     *
     * @return A list of {@link ServiceDTO} objects representing all the services. Returns an empty list if no services are available.
     */
    List<ServiceDTO> findAllServices();

    /**
     * Creates a new service with the specified details.
     *
     * @param serviceDTO A {@link ServiceDTO} object containing the details of the service to be created.
     * @return A {@link ServiceDTO} representing the newly created service.
     */
    ServiceDTO createService(ServiceDTO serviceDTO);

    /**
     * Updates an existing service with the specified details.
     *
     * @param id The unique identifier of the service to update.
     * @param serviceDTO A {@link ServiceDTO} object containing the updated details of the service.
     * @return A {@link ServiceDTO} representing the updated service. Returns {@code null} if no service with the specified ID is found.
     */
    ServiceDTO updateService(Long id, ServiceDTO serviceDTO);

    /**
     * Deletes a service by its unique identifier.
     *
     * @param id The unique identifier of the service to delete.
     * @return A String message indicating the outcome of the deletion operation.
     */
    String deleteService(Long id);

    /**
     * Retrieves a list of services that match the specified search criteria.
     *
     * @param name The name criteria to filter the services. Can be {@code null} or empty to ignore this filter.
     * @param serviceId The unique identifier criteria to filter the services. Can be {@code null} to ignore this filter.
     * @return A list of {@link ServiceDTO} objects representing the services that match the specified criteria. Returns an empty list if no matching services are found.
     */
    List<ServiceDTO> findServicesByCriteria(String name, Long serviceId);
}
