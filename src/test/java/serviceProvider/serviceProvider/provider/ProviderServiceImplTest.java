package serviceProvider.serviceProvider.provider;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import serviceProvider.serviceProvider.exceptions.ProviderNotFoundException;
import serviceProvider.serviceProvider.provider.model.ProviderDTO;
import serviceProvider.serviceProvider.provider.model.ProviderEntity;
import serviceProvider.serviceProvider.service.ServiceRepository;
import serviceProvider.serviceProvider.service.model.ServiceEntity;
import serviceProvider.serviceProvider.service.model.ServiceWithoutProvidersDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProviderServiceImplTest {

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private ProviderServiceImpl providerService;

    @Captor
    ArgumentCaptor<ProviderEntity> providerArgumentCaptor;

    private ProviderEntity providerEntity;
    private ProviderDTO providerDTO;
    private Long providerId = 1L;

    @BeforeEach
    void setUp() {
        providerEntity = new ProviderEntity();
        providerEntity.setId(providerId);
        providerEntity.setName("Test Provider");

        providerDTO = new ProviderDTO();
        providerDTO.setId(providerId);
        providerDTO.setName("Test Provider");
    }

    @Test
    void whenFindProviderById_thenReturnProviderDTO() {
        when(providerRepository.findByIdWithServices(providerId)).thenReturn(Optional.of(providerEntity));

        ProviderDTO resultDTO = providerService.findProviderById(providerId);

        assertNotNull(resultDTO);
        assertEquals(providerEntity.getId(), resultDTO.getId());
        assertEquals(providerEntity.getName(), resultDTO.getName());
    }

    @Test
    void whenFindProviderById_thenThrowProviderNotFoundException() {
        when(providerRepository.findByIdWithServices(providerId)).thenReturn(Optional.empty());

        assertThrows(ProviderNotFoundException.class, () -> {
            providerService.findProviderById(providerId);
        });
    }

    @Test
    void whenFindAllProviders_thenReturnProviderDTOList() {
        when(providerRepository.findAllWithServices()).thenReturn(Collections.singletonList(providerEntity));

        List<ProviderDTO> resultDTOList = providerService.findAllProviders();

        assertFalse(resultDTOList.isEmpty());
        assertEquals(1, resultDTOList.size());
        assertEquals(providerEntity.getName(), resultDTOList.get(0).getName());
    }

    @Test
    void whenCreateProvider_thenReturnProviderDTO() {
        when(providerRepository.save(any(ProviderEntity.class))).thenReturn(providerEntity);

        ProviderDTO createdProviderDTO = providerService.createProvider(providerDTO);

        assertNotNull(createdProviderDTO);
        assertEquals(providerDTO.getName(), createdProviderDTO.getName());
    }

    @Test
    void whenUpdateProvider_thenReturnUpdatedProviderDTO() {
        when(providerRepository.findByIdWithServices(providerId)).thenReturn(Optional.of(providerEntity));
        when(providerRepository.save(any(ProviderEntity.class))).thenReturn(providerEntity);

        ProviderDTO updatedProviderDTO = providerService.updateProvider(providerId, providerDTO);

        assertNotNull(updatedProviderDTO);
        assertEquals(providerDTO.getName(), updatedProviderDTO.getName());
    }

    @Test
    void whenUpdateProvider_thenThrowProviderNotFoundException() {
        when(providerRepository.findByIdWithServices(providerId)).thenReturn(Optional.empty());

        assertThrows(ProviderNotFoundException.class, () -> {
            providerService.updateProvider(providerId, providerDTO);
        });
    }

    @Test
    void whenDeleteProvider_thenSuccess() {
        when(providerRepository.findByIdWithServices(providerId)).thenReturn(Optional.of(providerEntity));

        String response = providerService.deleteProvider(providerId);

        assertEquals("Provider with id: " + providerId + " deleted successfully!", response);
        verify(providerRepository).deleteById(providerId);
    }

    @Test
    void whenDeleteProvider_thenThrowProviderNotFoundException() {
        when(providerRepository.findByIdWithServices(providerId)).thenReturn(Optional.empty());
        assertThrows(ProviderNotFoundException.class, () -> providerService.deleteProvider(providerId));
    }

    @Test
    void whenFindProvidersByCriteria_thenReturnProviderDTOList() {
        List<ProviderEntity> providerEntities = Collections.singletonList(providerEntity);
        when(providerRepository.findAll(any(Specification.class))).thenReturn(providerEntities);

        List<ProviderDTO> resultDTOList = providerService.findProvidersByCriteria("Test", 1L);

        assertFalse(resultDTOList.isEmpty());
        assertEquals(providerEntities.size(), resultDTOList.size());
        assertEquals(providerEntities.get(0).getName(), resultDTOList.get(0).getName());
    }

    @Test
    void whenFindProvidersByCriteria_withNoMatches_thenReturnEmptyList() {
        when(providerRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());

        List<ProviderDTO> resultDTOList = providerService.findProvidersByCriteria("NonExistent", 2L);

        assertTrue(resultDTOList.isEmpty());
    }

    @Test
    void whenUpdateProvider_thenUpdateProviderServices() {
        Long providerId = 1L;
        ProviderEntity existingProvider = new ProviderEntity();
        existingProvider.setId(providerId);
        existingProvider.setName("Provider Name");
        Set<ServiceEntity> existingServices = new HashSet<>();
        ServiceEntity existingService = new ServiceEntity(1L, "Existing Service");
        existingServices.add(existingService);
        existingProvider.setServices(existingServices);

        Set<ServiceWithoutProvidersDTO> newServiceDTOs = new HashSet<>();
        newServiceDTOs.add(new ServiceWithoutProvidersDTO(2L, "New Service"));

        ProviderDTO providerDTO = new ProviderDTO();
        providerDTO.setId(providerId);
        providerDTO.setName("Provider Name Updated");
        providerDTO.setServices(newServiceDTOs);

        List<ServiceEntity> newServices = newServiceDTOs.stream()
                                                        .map(dto -> new ServiceEntity(dto.getId(), dto.getDescription()))
                                                        .collect(Collectors.toList());

        when(providerRepository.findByIdWithServices(providerId)).thenReturn(Optional.of(existingProvider));
        when(serviceRepository.findAllById(anySet())).thenReturn(newServices);
        when(providerRepository.save(any(ProviderEntity.class))).thenReturn(existingProvider);

        ProviderDTO updatedProviderDTO = providerService.updateProvider(providerId, providerDTO);

        assertNotNull(updatedProviderDTO);
        verify(providerRepository).save(providerArgumentCaptor.capture());
        ProviderEntity savedProvider = providerArgumentCaptor.getValue();

        assertTrue(savedProvider.getServices().containsAll(newServices));
        assertEquals(newServices.size(), savedProvider.getServices().size());
    }
}