package serviceProvider.serviceProvider.provider.model;

import java.util.HashSet;
import java.util.Set;

import serviceProvider.serviceProvider.service.model.ServiceWithoutProvidersDTO;

public class ProviderDTO {

    private Long id;
    private String name;
    private Set<ServiceWithoutProvidersDTO> services = new HashSet<>();

    public ProviderDTO() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ServiceWithoutProvidersDTO> getServices() {
        return services;
    }

    public void setServices(Set<ServiceWithoutProvidersDTO> services) {
        this.services = services;
    }
}
