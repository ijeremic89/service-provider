package serviceProvider.serviceProvider.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import serviceProvider.serviceProvider.exceptions.ServiceNotFoundException;
import serviceProvider.serviceProvider.provider.ProviderRepository;
import serviceProvider.serviceProvider.service.model.ServiceDTO;
import serviceProvider.serviceProvider.service.model.ServiceEntity;
import serviceProvider.serviceProvider.service.projection.ServiceDescriptionProjection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ServiceServiceImplTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ProviderRepository providerRepository;

    @InjectMocks
    private ServiceServiceImpl serviceService;

    private Long serviceId;
    private ServiceEntity serviceEntity;

    @BeforeEach
    public void setUp() {
        // Common setup for all tests
        serviceId = 1L;
        serviceEntity = new ServiceEntity();
        serviceEntity.setId(serviceId);
        serviceEntity.setDescription("Service 1");

        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setId(serviceId);
        serviceDTO.setDescription("Service 1");
    }

    @Test
    public void whenFindServiceById_thenReturnServiceDTO() {
        when(serviceRepository.findByIdWithProviders(serviceId)).thenReturn(Optional.of(serviceEntity));
        ServiceDTO result = serviceService.findServiceById(serviceId);
        assertNotNull(result);
        assertEquals(serviceId, result.getId());
    }

    @Test
    public void whenFindServiceById_thenThrowException() {
        Long invalidServiceId = 99L; // Specific setup for this test
        when(serviceRepository.findByIdWithProviders(invalidServiceId)).thenReturn(Optional.empty());
        assertThrows(ServiceNotFoundException.class, () -> serviceService.findServiceById(invalidServiceId));
    }

    @Test
    public void whenFindAllServices_thenReturnServiceDTOList() {
        ServiceEntity service2 = new ServiceEntity();
        service2.setDescription("Service 2");
        when(serviceRepository.findAllWithProviders()).thenReturn(List.of(serviceEntity, service2));
        List<ServiceDTO> results = serviceService.findAllServices();
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals("Service 1", results.get(0).getDescription());
        assertEquals("Service 2", results.get(1).getDescription());
    }

    @Test
    public void whenCreateService_thenSaveService() {
        // Arrange
        ServiceDTO serviceDTOToCreate = new ServiceDTO();
        serviceDTOToCreate.setDescription("Sample Service");

        ServiceEntity savedServiceEntity = new ServiceEntity();
        savedServiceEntity.setId(1L);
        savedServiceEntity.setDescription(serviceDTOToCreate.getDescription());

        when(serviceRepository.save(any(ServiceEntity.class))).thenReturn(savedServiceEntity);

        // Act
        ServiceDTO createdService = serviceService.createService(serviceDTOToCreate);

        // Assert
        assertNotNull(createdService, "The created service DTO should not be null");
        assertEquals(savedServiceEntity.getDescription(), createdService.getDescription(), "The service description should match the DTO description");
        assertEquals(savedServiceEntity.getId(), createdService.getId(), "The saved service ID should be reflected in the returned DTO");

        // Verify interactions
        verify(serviceRepository).save(any(ServiceEntity.class));
    }

    @Test
    public void whenUpdateService_thenServiceUpdated() {
        // Arrange
        Long existingServiceId = 1L;
        ServiceEntity existingServiceEntity = new ServiceEntity();
        existingServiceEntity.setId(existingServiceId);
        existingServiceEntity.setDescription("Old Description");

        ServiceDTO serviceDTOToUpdate = new ServiceDTO();
        serviceDTOToUpdate.setDescription("Updated Description");

        when(serviceRepository.findByIdWithProviders(existingServiceId)).thenReturn(Optional.of(existingServiceEntity));
        when(serviceRepository.save(any(ServiceEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ServiceDTO updatedService = serviceService.updateService(existingServiceId, serviceDTOToUpdate);

        // Assert
        assertNotNull(updatedService, "The updated service DTO should not be null");
        assertEquals("Updated Description", updatedService.getDescription(), "The service description should be updated");

        // Verify interactions
        verify(serviceRepository).findByIdWithProviders(existingServiceId);
        verify(serviceRepository).save(any(ServiceEntity.class));
    }

    @Test
    public void whenDeleteService_thenServiceDeleted() {
        when(serviceRepository.findByIdWithProviders(serviceId)).thenReturn(Optional.of(serviceEntity));
        doNothing().when(serviceRepository).deleteById(serviceId);
        String response = serviceService.deleteService(serviceId);
        assertNotNull(response);
        assertTrue(response.contains(serviceId.toString()));
    }

    @Test
    public void whenFindServicesByCriteria_thenReturnFilteredServices() {
        String description = "Test Service";
        Long providerId = 1L;
        List<ServiceEntity> serviceEntities = Arrays.asList(serviceEntity); // Adjusted to use the shared serviceEntity
        when(serviceRepository.findAll(any(Specification.class))).thenReturn(serviceEntities);
        List<ServiceDTO> services = serviceService.findServicesByCriteria(description, providerId);
        assertFalse(services.isEmpty());
        // Additional assertions based on expected outcome
    }

    @Test
    public void whenFindAllServiceDescriptions_thenReturnDescriptions() {
        List<ServiceDescriptionProjection> projections = Arrays.asList(() -> "Service 1", () -> "Service 2");
        when(serviceRepository.findAllServiceDescriptions()).thenReturn(projections);
        List<String> descriptions = serviceService.findAllServiceDescriptions();
        assertEquals(2, descriptions.size());
        assertTrue(descriptions.contains("Service 1"));
        assertTrue(descriptions.contains("Service 2"));
    }
}